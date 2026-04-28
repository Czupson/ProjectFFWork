package payment;

import money.Money;

public class CardPayment extends Payment {
    private final String last4;

    public CardPayment(Money amount, String paymentId,String last4) {
        super(amount, paymentId);
        if (last4 == null || !last4.matches("\\d{4}")) {
            throw new IllegalArgumentException("last4 must be exactly 4 digits");
        }
        this.last4 = last4;
    }

    public String getLast4() {
        return last4;
    }

    @Override
    public void capture(){
        if (status != PaymentStatus.INITIATED){
            throw new IllegalStateException("Payment already processed");
        }

        status = PaymentStatus.CAPTURED;
    }
}
