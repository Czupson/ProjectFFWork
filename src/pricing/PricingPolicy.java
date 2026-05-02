package pricing;

import domain.resource.Resource;
import money.Money;

import java.time.LocalDateTime;

public interface PricingPolicy {
    Money price(Resource resource, LocalDateTime start, LocalDateTime end);
}
