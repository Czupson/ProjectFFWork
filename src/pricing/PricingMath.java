package pricing;

import domain.resource.Resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

final class PricingMath {
    private PricingMath() {}

    static BigDecimal basePrice(Resource resource, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(resource, "Resource cannot be null");
        Objects.requireNonNull(start, "Start cannot be null");
        Objects.requireNonNull(end, "End cannot be null");

        int minutes = (int) ChronoUnit.MINUTES.between(start, end);

        BigDecimal hourlyRate = resource
                .hourlyRate()
                .getAmount();

        BigDecimal pricePerMinute = hourlyRate.divide(
                BigDecimal.valueOf(60),
                10,
                RoundingMode.HALF_UP
        );

        return pricePerMinute.multiply(BigDecimal.valueOf(minutes));
    }
}
