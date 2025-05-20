package contracts;

import objects.LegalForm;
import objects.Person;
import company.InsuranceCompany;
import payment.ContractPaymentData;

//importy standartnych kniznic java
import java.util.HashSet;
import java.util.Set;


public class TravelContract extends AbstractContract {


    private final Set<Person> insuredPersons;

    public TravelContract(String contractNumber,
                          InsuranceCompany insurer,
                          Person policyHolder,
                          ContractPaymentData contractPaymentData,
                          int coverageAmount,
                          Set<Person> personsToInsure) {

        super(contractNumber, insurer, policyHolder, contractPaymentData, coverageAmount);


        if (personsToInsure == null || personsToInsure.isEmpty()) {
            throw new IllegalArgumentException("personsToInsure cannot be null or empty, error -_-");
        }

        if (contractPaymentData == null) {
            throw new IllegalArgumentException("contractPaymentData cannot be null, error -_-");
        }
        for (Person person : personsToInsure) {
            if(person.getLegalForm() != LegalForm.NATURAL) {
                throw new IllegalArgumentException("All persons to insure must be NATURAL");
            }
        }

      //copy all objects personsToInsure to HashSet<>
        this.insuredPersons = new HashSet<>(personsToInsure);
    }

    public Set<Person> getInsuredPersons() {
        return insuredPersons;
    }
}
