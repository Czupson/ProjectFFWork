package pricing;

import domain.resource.Resource;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class HappyHoursPricing implements PricingPolicy {
    private static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.70);

    private static final int HAPPY_HOURS_START = 14;
    private static final int HAPPY_HOURS_END = 16;

    @Override
    public Money price(Resource resource, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(resource, "Resource cannot be null");
        Objects.requireNonNull(start, "Start cannot be null");
        Objects.requireNonNull(end, "End cannot be null");

        BigDecimal total = PricingMath.basePrice(resource, start, end);

        if (isHappyHours(start)) {
            total = total.multiply(DISCOUNT_RATE);
        }

        return new Money(total.setScale(2, RoundingMode.HALF_UP));
    }

    private boolean isHappyHours(LocalDateTime start) {
        int hour = start.getHour();
        return hour >= HAPPY_HOURS_START && hour < HAPPY_HOURS_END;
    }
}
