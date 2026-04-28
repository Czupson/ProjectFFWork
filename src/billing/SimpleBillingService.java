package billing;

import domain.booking.Booking;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleBillingService implements Billable {

    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    @Override
    public Invoice toInvoice(Booking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        if (booking.getPayment() == null) {
            throw new IllegalStateException("Cannot create invoice for unpaid booking");
        }

        String number = "INV-" +
                LocalDateTime.now().toLocalDate().toString().replace("-", "") +
                "-" + COUNTER.getAndIncrement();

        String desc = "Reservation " +
                booking.getResource().getName() + " " +
                booking.getStart() + " - " +
                booking.getEnd();

        return new Invoice(
                number,
                LocalDateTime.now(),
                booking.getUser(),
                booking.getCalculatedPrice(),
                desc
        );
    }
}