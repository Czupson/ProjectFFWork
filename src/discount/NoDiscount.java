package discount;

import domain.booking.Booking;
import money.Money;

public class NoDiscount implements Discountable {

    @Override
    public Money apply(Money basePrice, Booking booking) {
        return basePrice;
    }
}