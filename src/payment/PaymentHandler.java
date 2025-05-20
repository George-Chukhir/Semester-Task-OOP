package payment;

import company.InsuranceCompany;
import contracts.*;
import java.util.*;

//Trieda pre realizaciu a spracovanie platieb
public class PaymentHandler {

    private final InsuranceCompany insurer;
    private final Map<AbstractContract, Set<PaymentInstance>> paymentHistory;

    public PaymentHandler(InsuranceCompany insurer) {
        if (insurer == null) {
            throw new IllegalArgumentException("insurer cannot be null");
        }

        this.insurer = insurer;
        this.paymentHistory = new HashMap<>();
    }


    public void pay(AbstractContract contract, int amount) {
        if (contract == null || amount <= 0) {
            throw new IllegalArgumentException("contract cannot be null or amount <= 0");
        }
        if(contract.getInsurer() != insurer || !contract.isActive()){
            throw new InvalidContractException("Contract doesn't belong to this insurer or contract is inactive ");
        }


        ContractPaymentData data = contract.getContractPaymentData();


        data.setOutstandingBalance(data.getOutstandingBalance() - amount);

        //novy zapis
        PaymentInstance instance = new PaymentInstance(insurer.getCurrentTime(), amount);

        //adding new record to TreeSet
        paymentHistory.computeIfAbsent(contract, contractKey -> new TreeSet<>()).add(instance);


    }





    public void pay(MasterVehicleContract contract, int amount) {
        if (contract == null || amount <= 0) {
            throw new IllegalArgumentException("contract cannot be null or amount is non-positive");
        }
        if (contract.getInsurer() != insurer || !contract.isActive() || contract.getChildContracts().isEmpty()) {
            throw new InvalidContractException("Contract doesn't belong to this insurer or contract is inactive or has't child contracts");
        }

        //made copy of initial value
        final int initialAmount = amount;


        Set<SingleVehicleContract> children = contract.getChildContracts();

        // Step 1: Splatenie dlhov
        for(SingleVehicleContract childContract : children){
            if(childContract.isActive()){

                ContractPaymentData data = childContract.getContractPaymentData();

                //pomocka
                //balance - aktualny nedoplatok (kolko treba zaplatit)
                //amount - vyska ktoru poistnik chce zaplatit
                //premium - vyska ktoru treba zaplatit za jeden period zmluvy

                int balance = data.getOutstandingBalance();

                if(balance > 0){
                    if(amount >= balance){
                        data.setOutstandingBalance(0);
                        amount-=balance;
                    }
                    else{
                        data.setOutstandingBalance(balance - amount);
                        amount=0;
                        break;
                    }
                }
            }
        }

        // Step 2: overpayments
        while(amount > 0){
            boolean paymentMade = false;
            for(SingleVehicleContract childContract : children) {

                // preskočí
                if (!childContract.isActive()) continue;

                //pomocka
                //balance - aktualny nedoplatok (kolko treba zaplatit)
                //amount - celkova vyska ktoru poistnik chce zaplatit
                //premium - vyska ktoru treba zaplatit za jeden period zmluvy

                ContractPaymentData data = childContract.getContractPaymentData();

                int premium = data.getPremium();
                if (amount > premium) {
                    data.setOutstandingBalance(data.getOutstandingBalance() - premium);
                    amount -= premium;
                    paymentMade = true;
                } else {
                    data.setOutstandingBalance(data.getOutstandingBalance() - amount);
                    amount = 0;
                    paymentMade = true;
                    break;
                }
            }

            //ak ani jeden cyklus nebol aktivovany, to znamena ze alebo vsetky kontrakty su zaplatene, alebo vsetky su neaktivne
            //cize nemame kam smerovat peniaze
            if(!paymentMade){
                break;
            }
        }

    // realizacia platieb
        PaymentInstance instance = new PaymentInstance(insurer.getCurrentTime(), initialAmount);
        paymentHistory.computeIfAbsent(contract, contractKey -> new TreeSet<>()).add(instance);

    }

    public Map<AbstractContract, Set<PaymentInstance>> getPaymentHistory() {
        return paymentHistory;
    }



}



