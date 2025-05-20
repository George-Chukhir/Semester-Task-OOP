package objects;

import contracts.AbstractContract;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Person {

    private final String id;
    private final LegalForm legalForm;
    private int paidOutAmount;
    private final Set<AbstractContract> contracts;

    public Person(String id) {
        if(id == null || id.isEmpty())
        {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if(isValidBirthNumber(id)) {
            this.legalForm = LegalForm.NATURAL;
        }
        else if(isValidRegistrationNumber(id)) {
            this.legalForm = LegalForm.LEGAL;
        }
        else{
            throw new IllegalArgumentException("Invalid id");
        }

        this.id = id;
        this.contracts = new LinkedHashSet<>();
        this.paidOutAmount = 0;
    }

    public String getId() {
        return id;
    }

    public int getPaidOutAmount() {
        return paidOutAmount;
    }

    public LegalForm getLegalForm() {
        return legalForm;
    }

    public Set<AbstractContract> getContracts() {
        return contracts;
    }

    public void addContract(AbstractContract contract) {
        if(contract == null){
            throw new IllegalArgumentException("Contract cannot be null");
        }
        contracts.add(contract);
    }

    public void payout(int paidOutAmount) {
        if(paidOutAmount <= 0){
            throw new IllegalArgumentException("Paid Out Amount cannot be non-positive");
        }
        this.paidOutAmount += paidOutAmount;
    }


    public boolean isValidBirthNumber(String birthNumber) {
        if (birthNumber == null || (birthNumber.length() != 10 && birthNumber.length() != 9)) return false;

        //Vsetky symboly musia byt cisla!
        if (!birthNumber.chars().allMatch(Character::isDigit)) return false;



        int year = Integer.parseInt(birthNumber.substring(0, 2));
        int month = Integer.parseInt(birthNumber.substring(2, 4));
        int day = Integer.parseInt(birthNumber.substring(4, 6));


        //9 numbers and > 53 is invalid
        if (birthNumber.length() == 9) {
            if(year > 53){
                return false;
            }
        }

        if (month >= 51 && month <=62 ) month -= 50;
        if (!(month >= 1 && month <= 12)) return false;


        int fullYear;
        if (birthNumber.length() == 9) { //Always 19xx
            fullYear = 1900 + year;
        }
        else {
            if(year <= 53){ // 10 numbers and <53 always 20xx
                fullYear = 2000 + year;
            }
            else{ //10 numbers and > 53
                fullYear = 1900 + year;
            }
        }

        // kontrola na existenciu datumu
        try {
            LocalDate.of(fullYear, month, day);
        } catch (DateTimeException e) {
            return false;
        }


        if(birthNumber.length() == 10){
            int sum = 0;
            for(int i = 0; i < 10; i++){
                int digit = birthNumber.charAt(i) - '0';
                sum += ((i % 2 == 0)? digit : -digit);
            }
            if (sum % 11 != 0) {
                return false;
            }
        }

        return true;
    }


    public boolean isValidRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null) {
            return false;
        }

        return(registrationNumber.length() == 6 || registrationNumber.length() == 8)
                && registrationNumber.chars().allMatch(Character::isDigit);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //Skontroluje, či je to ten istý objekt?
        if(!(o instanceof Person)) return false; // Skontroluje, či je o naozaj objekt typu Person? alebo napr. Vehicle
        Person person = (Person) o; //priradí o k typu Person na prístup k jeho id
        return id.equals(person.id); //Ak majú dve osoby rovnaké ID, považujem ich za tú istú osobu
    }

    @Override
    public int hashCode() {
        //Táto metóda vytvorí unikátne  číslo (hashcode) pre objekt Person
        return Objects.hash(id);
    }



}
