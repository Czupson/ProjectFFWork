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

    public void refund() {
        if (status != PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Only captured payment can be refunded");
        }
        status = PaymentStatus.REFUNDED;
    }
}
