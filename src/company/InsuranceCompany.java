package company;

import contracts.*;
import objects.*;
import payment.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class InsuranceCompany {

    private final Set<AbstractContract> contracts;
    private final PaymentHandler handler;
    private LocalDateTime currentTime;

    public InsuranceCompany(LocalDateTime currentTime)   {
        if (currentTime == null) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        this.currentTime = currentTime;
        this.contracts = new LinkedHashSet<>();
        this.handler = new PaymentHandler(this);
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        if (currentTime == null) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        this.currentTime = currentTime;
    }

    public Set<AbstractContract> getContracts() {
        return contracts;
    }

    public PaymentHandler getHandler() {
        return handler;
    }

    public SingleVehicleContract insureVehicle(String contractNumber,
                                               Person beneficiary,
                                               Person policyHolder,
                                               int proposedPremium,
                                               PremiumPaymentFrequency proposedPremiumFrequency,
                                               Vehicle vehicleToInsure) {


        if (contractNumber == null || policyHolder == null || proposedPremium <= 0 ||
                proposedPremiumFrequency == null || vehicleToInsure == null)  {
            throw new IllegalArgumentException("Some argument is invalid(null), error -_-");
        }

        for (AbstractContract contract : contracts) {
            if (contract.getContractNumber().equals(contractNumber)) {
                throw new IllegalArgumentException("Same contract number, error -_-");
            }
        }


        int annualPayment = (proposedPremium * 12) / proposedPremiumFrequency.getValueInMonths();
        int minimumRequired = (vehicleToInsure.getOriginalValue() * 2) / 100; // min 2% od vysky auta

        if (annualPayment < minimumRequired) {
            throw new IllegalArgumentException("Proposed payment is too small, error -_-");
        }

        ContractPaymentData paymentData = new ContractPaymentData(proposedPremium, proposedPremiumFrequency, currentTime,
                0);

        //vyska vyplaty ak nieco stane
        int coverageAmount = vehicleToInsure.getOriginalValue() / 2;

        SingleVehicleContract contract = new SingleVehicleContract(contractNumber, this, beneficiary, policyHolder,
                paymentData, coverageAmount, vehicleToInsure );

        this.contracts.add(contract);
        policyHolder.addContract(contract);
        this.chargePremiumOnContract(contract);

        return contract;
    }


    public TravelContract insurePersons(String contractNumber,
                                        Person policyHolder,
                                        int proposedPremium,
                                        PremiumPaymentFrequency premiumPaymentFrequency,
                                        Set<Person> personsToInsure) {

        if (contractNumber == null || policyHolder == null || premiumPaymentFrequency == null ||
                personsToInsure.isEmpty() || proposedPremium <= 0)  {
            throw new IllegalArgumentException("Some argument is invalid(null), error -_-");
        }

        for (AbstractContract contract : contracts) {
            if (contract.getContractNumber().equals(contractNumber)) {
                throw new IllegalArgumentException("Some argument is invalid(null), error -_-");
            }
        }

        for (Person person : personsToInsure) {
            if(person == null || person.getLegalForm() != LegalForm.NATURAL){
                throw new IllegalArgumentException("All persons must be natural and not null");
            }
        }


        int annualPayment = (proposedPremium * 12) / premiumPaymentFrequency.getValueInMonths();
        if (annualPayment < 5 * personsToInsure.size()) {
            throw new IllegalArgumentException("Proposed payment is too small, error -_-");
        }

        ContractPaymentData paymentData = new ContractPaymentData(proposedPremium, premiumPaymentFrequency, currentTime,
                0);

        //vyska vyplaty ak nieco stane
        int coverageAmount = personsToInsure.size() * 10;


        TravelContract contract = new TravelContract(contractNumber, this, policyHolder,
                paymentData, coverageAmount, personsToInsure);

        this.contracts.add(contract);
        policyHolder.addContract(contract);
        this.chargePremiumOnContract(contract);

        return contract;
    }


    public MasterVehicleContract createMasterVehicleContract(String contractNumber,
                                                             Person beneficiary,
                                                             Person policyHolder) {
        for (AbstractContract contract : contracts) {
            if (contract.getContractNumber().equals(contractNumber)) {
                throw new IllegalArgumentException("Some argument is invalid(null), error -_-");
            }
        }

        MasterVehicleContract contract = new MasterVehicleContract(contractNumber, this, beneficiary, policyHolder);

        this.contracts.add(contract);
        policyHolder.addContract(contract);

        return contract;
    }


    public void moveSingleVehicleContractToMasterVehicleContract(MasterVehicleContract masterVehicleContract,
                                                                 SingleVehicleContract singleVehicleContract) {
        if (masterVehicleContract == null || singleVehicleContract == null) {
            throw new IllegalArgumentException("Some argument is invalid(null), error -_-");
        }
        if(masterVehicleContract.getInsurer() != this || singleVehicleContract.getInsurer() != this){
            throw new InvalidContractException("Contracts don't belong to that insurance company, error -_-");
        }
        if (!masterVehicleContract.isActive() || !singleVehicleContract.isActive()) {
            throw new InvalidContractException("Contracts must be active,error -_-");
        }
        if (!masterVehicleContract.getPolicyHolder().equals(singleVehicleContract.getPolicyHolder())) {
            throw new InvalidContractException("Contracts don't belong to one person, error -_-");
        }

        masterVehicleContract.requestAdditionOfChildContract(singleVehicleContract);
        contracts.remove(singleVehicleContract);
        singleVehicleContract.getPolicyHolder().getContracts().remove(singleVehicleContract);
    }


    public void chargePremiumsOnContracts(){
        //Urobil kopiu mnoziny pre bezbecnost udajov, aby nebolo moznosti modifikovat povodnu mnozinu
        Set<AbstractContract> contractsCopy  = new LinkedHashSet<>(this.contracts);

        for (AbstractContract contract : contractsCopy) {
            if (contract.isActive()) {
                contract.updateBalance();
            }
        }
    }


    public void chargePremiumOnContract(AbstractContract contract){


        ContractPaymentData paymentData = contract.getContractPaymentData();
        if (paymentData == null) return;

        while(paymentData.getNextPaymentTime() != null  && !paymentData.getNextPaymentTime().isAfter(currentTime)){
            paymentData.setOutstandingBalance(paymentData.getOutstandingBalance() + paymentData.getPremium());
            paymentData.updateNextPaymentTime();
        }

    }


    public void chargePremiumOnContract(MasterVehicleContract contract){

        Set<SingleVehicleContract> copyOfChildContracts = new LinkedHashSet<>(contract.getChildContracts());

        for(SingleVehicleContract childContract : copyOfChildContracts){
            if (childContract.isActive()) {
                this.chargePremiumOnContract((AbstractContract) childContract);
            }
        }
    }

    public void processClaim(SingleVehicleContract singleVehicleContract, int expectedDamages){
        if(singleVehicleContract == null || expectedDamages <= 0 ){
            throw new IllegalArgumentException("Contract is null or expected damage is negative");
        }

        if(!singleVehicleContract.isActive()){
            throw new InvalidContractException("Contract is not active, error -_-");
        }


        //If payout is more that 70% of value, it's gg
        int percentage = expectedDamages * 100 / singleVehicleContract.getInsuredVehicle().getOriginalValue();
        if(percentage >= 70){
            singleVehicleContract.setInactive();
        }

        // Define to whom we should pay money
        Person recipient;
        if (singleVehicleContract.getBeneficiary() != null) {
            recipient = singleVehicleContract.getBeneficiary();
        }
        else{
            recipient = singleVehicleContract.getPolicyHolder();
        }

        recipient.payout(singleVehicleContract.getCoverageAmount());
    }



    
    public void processClaim(TravelContract travelContract, Set<Person> affectedPersons){
        if(travelContract == null || affectedPersons == null || affectedPersons.isEmpty()){
            throw new IllegalArgumentException("TravelContract is null or affectedPersons is null or empty");
        }
        if (!travelContract.getInsuredPersons().containsAll(affectedPersons)) {
            throw new IllegalArgumentException("Affected persons must be a subset of insured persons in the travel contract.");
        }
        if(!travelContract.isActive()){
            throw new InvalidContractException("Contract is not active, error -_-");
        }





        int paymentAmount = travelContract.getCoverageAmount() / affectedPersons.size();

        for(Person affectedPerson : affectedPersons){
            affectedPerson.payout(paymentAmount);
        }

        travelContract.setInactive();
    }


}
