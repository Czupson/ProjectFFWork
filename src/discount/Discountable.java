package discount;

import domain.booking.Booking;
import money.Money;

public interface Discountable {
    Money apply(Money basePrice, Booking booking);
}