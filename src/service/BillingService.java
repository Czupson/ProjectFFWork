package service;

import billing.Billable;
import billing.Invoice;
import domain.booking.Booking;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class BillingService implements Billable {

    private final AtomicInteger counter = new AtomicInteger(1);

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

        String number = "INV-" + date + "-" + counter.getAndIncrement();

        String desc = String.format(
                "Reservation %s %s - %s",
                booking.getResource().getName(),
                booking.getStart(),
                booking.getEnd()
        );

        return new Invoice(
                number,
                now,
                booking.getUser(),
                booking.getCalculatedPrice(),
                desc
        );
    }
}