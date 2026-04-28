package payment;

import money.Money;

public class WalletPayment extends Payment {

    public WalletPayment(Money amount, String paymentId) {
        super(amount, paymentId);
    }

    @Override
    public void capture() {
        ensureNotCaptured();
        status = PaymentStatus.CAPTURED;
    }
}