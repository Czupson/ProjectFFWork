package billing;

import domain.booking.Booking;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleBillingService implements Billable {
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private static final String INVOICE_PREFIX = "INV-";
    private static final String DESCRIPTION_PREFIX = "Reservation ";

    @Override
    public Invoice toInvoice(Booking booking) {

        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }

        if (booking.getPayment() == null) {
            throw new IllegalStateException("Cannot create invoice for unpaid booking");
        }

        LocalDateTime now = LocalDateTime.now();

        String date = String.format("%04d%02d%02d",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth());

        String number = INVOICE_PREFIX + date + "-" + COUNTER.getAndIncrement();

        String desc = DESCRIPTION_PREFIX +
                booking.getResource().getName() + " " +
                booking.getStart() + " - " +
                booking.getEnd();

        return new Invoice(
                number,
                now,
                booking.getUser(),
                booking.getCalculatedPrice(),
                desc
        );
    }
}
