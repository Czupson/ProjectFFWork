package billing;

import domain.booking.Booking;

public interface Billable {
    billing.Invoice toInvoice(Booking booking);
}
