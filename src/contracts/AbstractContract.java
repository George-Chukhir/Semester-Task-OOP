package contracts;

import company.InsuranceCompany;
import objects.Person;
import payment.ContractPaymentData;

import java.util.Objects;


public abstract class AbstractContract {

    private final String contractNumber;
    protected final InsuranceCompany insurer;
    protected final Person policyHolder;
    protected final ContractPaymentData contractPaymentData;
    protected int coverageAmount;
    protected boolean isActive;



    public AbstractContract(String contractNumber,
                            InsuranceCompany insurer,
                            Person policyHolder,
                            ContractPaymentData contractPaymentData,
                            int coverageAmount) {

        if (contractNumber == null || contractNumber.isEmpty()) {
            throw new IllegalArgumentException("Contract number is null or empty");
        }
        if (insurer == null  || policyHolder == null ||  coverageAmount < 0) {
            throw new IllegalArgumentException("Contract data is null or empty");
        }
        this.contractNumber = contractNumber;
        this.insurer = insurer;
        this.policyHolder = policyHolder;
        this.contractPaymentData = contractPaymentData;
        this.coverageAmount = coverageAmount;
        this.isActive = true;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Person getPolicyHolder() {
        return policyHolder;
    }

    public InsuranceCompany getInsurer() {
        return insurer;
    }

    public int getCoverageAmount() {
        return coverageAmount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setInactive(){
        this.isActive = false;
    }

    public void setCoverageAmount(int coverageAmount) {
        if (coverageAmount < 0) {
            throw new IllegalArgumentException("Coverage amount can't be negative");
        }
        this.coverageAmount = coverageAmount;
    }

    public ContractPaymentData getContractPaymentData() {
        return contractPaymentData;
    }


    public void pay(int amount) {
        // Access InsuranceCompany via insurer, call getHandler() to get PaymentHandler,
        // then call pay(AbstractContract contract, int amount) on PaymentHandler
        insurer.getHandler().pay(this, amount);
    }

    public void updateBalance(){
        // Access InsuranceCompany via insurer, call chargePremiumOnContract(AbstractContract contract)
        insurer.chargePremiumOnContract(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof AbstractContract)) return false;
        AbstractContract abstractContract = (AbstractContract ) o;
        return Objects.equals(contractNumber, abstractContract.contractNumber);
    }

    @Override
    public int hashCode() {
        //vytvorí unikátne  číslo (hashcode) pre objekt AbstractContract
        return Objects.hash(contractNumber);
    }
}
