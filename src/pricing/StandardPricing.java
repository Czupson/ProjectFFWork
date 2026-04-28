package pricing;

import domain.booking.Booking;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class StandardPricing  implements PricingPolicy{
    @Override
    public Money price(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be  null");
        }

        int minutes = booking.durationMinutes();
        BigDecimal hourlyRate = booking.getResource().hourlyRate().getAmount();
        BigDecimal pricePerMinute = hourlyRate.divide(BigDecimal.valueOf(60), 10,
                RoundingMode.HALF_UP);

        BigDecimal total = pricePerMinute.multiply(BigDecimal.valueOf(minutes));
        return new Money(total.setScale(2, RoundingMode.HALF_UP));
    }
}
