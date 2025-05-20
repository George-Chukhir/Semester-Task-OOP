package contracts;

import objects.Person;
import objects.Vehicle;
import company.InsuranceCompany;
import payment.ContractPaymentData;


public class SingleVehicleContract extends AbstractVehicleContract{

    private final Vehicle insuredVehicle;

    public SingleVehicleContract(String contractNumber,
                                 InsuranceCompany insurer,
                                 Person beneficiary,
                                 Person policyHolder,
                                 ContractPaymentData contractPaymentData,
                                 int coverageAmount,
                                 Vehicle vehicleToInsure) {

        super(contractNumber, insurer, beneficiary, policyHolder, contractPaymentData, coverageAmount);

        if (vehicleToInsure == null || contractPaymentData == null) {
            throw new IllegalArgumentException("vehicleToInsure is null or contractPaymentData is null, it cannot be null ");
        }

        this.insuredVehicle = vehicleToInsure;
    }

    public Vehicle getInsuredVehicle() {
        return insuredVehicle;
    }

}
