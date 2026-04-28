package payment;

import money.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WalletPaymentTest {

    @Test
    void shouldCaptureWalletPayment() {
        WalletPayment payment = new WalletPayment(
                Money.of("100.00"),
                "PAY-1"
        );

        payment.capture();

        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
    }

    @Test
    void shouldRefundCapturedPayment() {
        WalletPayment payment = new WalletPayment(
                Money.of("100.00"),
                "PAY-1"
        );

        payment.capture();
        payment.refund();

        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }

    @Test
    void shouldThrowWhenRefundBeforeCapture() {
        WalletPayment payment = new WalletPayment(
                Money.of("100.00"),
                "PAY-1"
        );

        assertThrows(IllegalStateException.class, payment::refund);
    }
}