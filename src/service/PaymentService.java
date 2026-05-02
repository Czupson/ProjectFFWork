package service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import payment.CardPayment;
import payment.Payment;
import payment.WalletPayment;
import repo.BookingRepository;

public class PaymentService {
    private final BookingRepository bookingRepo;

    public PaymentService(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    public Payment pay(String bookingId, String last4) {
        validateBookingId(bookingId);
        validateLast4(last4);

        Booking booking = getConfirmedUnpaidBooking(bookingId);

        Payment payment = new CardPayment(
                booking.getCalculatedPrice(),
                "PAY-" + booking.getId(),
                last4
        );

        return processPayment(booking, payment);
    }

    public Payment payWithWallet(String bookingId) {
        validateBookingId(bookingId);

        Booking booking = getConfirmedUnpaidBooking(bookingId);

        Payment payment = new WalletPayment(
                booking.getCalculatedPrice(),
                "PAY-" + booking.getId()
        );

        return processPayment(booking, payment);
    }

    public void refund(String bookingId) {
        validateBookingId(bookingId);

        Booking booking = findBooking(bookingId);
        Payment payment = booking.getPayment();

        if (payment == null) {
            throw new IllegalStateException("No payment to refund");
        }

        if (payment instanceof WalletPayment wallet) {
            wallet.refund();
        } else {
            throw new IllegalStateException("Only wallet payments can be refunded");
        }
    }

    private void validateBookingId(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
    }

    private void validateLast4(String last4) {
        if (last4 == null || !last4.matches("\\d{4}")) {
            throw new IllegalArgumentException("Card last4 must be exactly 4 digits");
        }
    }

    private Booking getConfirmedUnpaidBooking(String bookingId) {
        Booking booking = findBooking(bookingId);

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED bookings can be paid");
        }

        if (booking.getPayment() != null) {
            throw new IllegalStateException("Booking already paid");
        }

        return booking;
    }

    private Booking findBooking(String bookingId) {
        return bookingRepo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    private Payment processPayment(Booking booking, Payment payment) {
        payment.capture();
        booking.attachPayment(payment);
        return payment;
    }
}
