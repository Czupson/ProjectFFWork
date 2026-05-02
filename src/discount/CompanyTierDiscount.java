package discount;

import domain.booking.Booking;
import domain.user.CompanyUser;
import money.Money;

import java.math.BigDecimal;

public class CompanyTierDiscount implements Discountable {
    private static final BigDecimal COMPANY_RATE = BigDecimal.valueOf(0.90);

    @Override
    public Money apply(Money basePrice, Booking booking) {
        if (booking.getUser() instanceof CompanyUser) {
            return basePrice.multiply(COMPANY_RATE);
        }
        return basePrice;
    }
}
