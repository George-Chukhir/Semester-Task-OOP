package contracts;

import company.InsuranceCompany;
import objects.LegalForm;
import objects.Person;


import java.util.LinkedHashSet;
import java.util.Set;




public class MasterVehicleContract extends AbstractVehicleContract {

    //childContracts je atribut triedy (mnozina vsetkych podriadenych zmluv).
    private final Set<SingleVehicleContract> childContracts;

    public MasterVehicleContract(String contractNumber,
                                 InsuranceCompany insurer,
                                 Person beneficiary,
                                 Person policyHolder){

        super(contractNumber, insurer, beneficiary, policyHolder, null, 0);


        if ( this.policyHolder.getLegalForm() != LegalForm.LEGAL ) {
            throw new IllegalArgumentException("Policy holder must be LEGAL");
        }

        //Inicializujem childContracts ako novu prazdnu mnozinu LinkedHashSet<>();.
        this.childContracts = new LinkedHashSet<>();
    }


    public Set<SingleVehicleContract> getChildContracts() {
        return childContracts;
    }


    public void requestAdditionOfChildContract(SingleVehicleContract contract) {
        insurer.moveSingleVehicleContractToMasterVehicleContract(this, contract);
    }

    @Override
    public boolean isActive(){

        if (childContracts.isEmpty()) {
            return this.isActive;
        }

        //for-each, lokalna premenna contract prejde vsetky prvky mnoziny childContracts
        for(SingleVehicleContract contract : childContracts){
            if(contract.isActive()){
                return true;
            }
        }

        return false;
    }

    @Override
    public void setInactive() {
        for (SingleVehicleContract contract : childContracts) {
            contract.setInactive();
        }
        this.isActive = false;
    }


    @Override
    public void pay(int amount) {
        insurer.getHandler().pay(this, amount);
    }

    @Override
    public void updateBalance() {
        insurer.chargePremiumOnContract(this);
    }

}
