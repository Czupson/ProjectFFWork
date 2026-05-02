package discount;

import domain.booking.Booking;
import domain.user.IndividualUser;
import money.Money;

import java.math.BigDecimal;

public class StudentDiscount implements Discountable {
    private static final BigDecimal STUDENT_RATE = BigDecimal.valueOf(0.80);

    @Override
    public Money apply(Money basePrice, Booking booking) {
        if (booking.getUser() instanceof IndividualUser) {
            return basePrice.multiply(STUDENT_RATE);
        }
        return basePrice;
    }
}
