package discount;

import domain.booking.Booking;
import domain.user.IndividualUser;
import money.Money;

import java.math.BigDecimal;

public class StudentDiscount implements Discountable {

    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(0.20);

    @Override
    public Money apply(Money basePrice, Booking booking) {
        if (booking.getUser() instanceof IndividualUser) {
            return basePrice.multiply(BigDecimal.ONE.subtract(DISCOUNT));
        }
        return basePrice;
    }
}