package payment;

import java.time.LocalDateTime;
import java.util.Objects;

//registruje platbu
public class PaymentInstance implements Comparable<PaymentInstance> {
    private final LocalDateTime paymentTime;
    private final int paymentAmount;

    public PaymentInstance(LocalDateTime paymentTime,
                           int paymentAmount) {

        if (paymentAmount <= 0 || paymentTime == null) {
            throw new IllegalArgumentException("Invalid payment time or payment amount");
        }

        this.paymentTime = paymentTime;
        this.paymentAmount = paymentAmount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    @Override
    //podl'a casu
    public int compareTo(PaymentInstance other){
        return this.paymentTime.compareTo(other.paymentTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof PaymentInstance)) return false;
        PaymentInstance that = (PaymentInstance) o;
        return paymentAmount == that.paymentAmount && paymentTime.equals(that.paymentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentTime, paymentAmount);
    }



}
