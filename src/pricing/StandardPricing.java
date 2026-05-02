package pricing;

import domain.resource.Resource;
import money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

public class StandardPricing implements PricingPolicy {
    @Override
    public Money price(Resource resource, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(resource, "Resource cannot be null");
        Objects.requireNonNull(start, "Start cannot be null");
        Objects.requireNonNull(end, "End cannot be null");

        BigDecimal total = PricingMath.basePrice(resource, start, end);

        return new Money(total.setScale(2, RoundingMode.HALF_UP));
    }
}
