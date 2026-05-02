package pricing;

import domain.resource.Room;
import money.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PricingTest {
    private Room createRoom() {
        return new Room("Room1", 10, Set.of(), Money.of("60"));
    }

    @Test
    void shouldCalculateStandardPrice() {
        StandardPricing pricing = new StandardPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        Money price = pricing.price(createRoom(), start, end);

        assertEquals(Money.of("120.00"), price);
    }

    @Test
    void shouldApplyHappyHoursDiscount() {
        HappyHoursPricing pricing = new HappyHoursPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 14, 0);
        LocalDateTime end = start.plusHours(2);

        Money price = pricing.price(createRoom(), start, end);

        assertEquals(Money.of("84.00"), price);
    }

    @Test
    void shouldNotApplyDiscountOutsideHappyHours() {
        HappyHoursPricing pricing = new HappyHoursPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        Money price = pricing.price(createRoom(), start, end);

        assertEquals(Money.of("120.00"), price);
    }

    @Test
    void shouldThrowWhenResourceIsNull() {
        StandardPricing pricing = new StandardPricing();

        assertThrows(NullPointerException.class,
                () -> pricing.price(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }
}
