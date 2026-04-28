package payment;

import money.Money;

public abstract class Payment {
    protected final Money amount;
    protected final String paymentId;
    protected PaymentStatus status;

    protected Payment(Money amount, String paymentId) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (paymentId == null || paymentId.isBlank()) {
            throw new IllegalArgumentException("PaymentID cannot be empty");
        }
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = PaymentStatus.INITIATED;
    }
    public Money getAmount() {
        return amount;
    }
    public String getPaymentId() {
        return paymentId;
    }
    public PaymentStatus getStatus() {
        return status;
    }
    public abstract void capture();

    public void refund() {
        if (status != PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Only captured payment can be refunded");
        }
        status = PaymentStatus.REFUNDED;
    }

    protected void ensureNotCaptured() {
        if (status == PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Payment already captured");
        }
    }
}
