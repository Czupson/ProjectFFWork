package pricing;

import domain.booking.Booking;
import domain.resource.Room;
import domain.user.IndividualUser;
import money.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PricingTest {

    private Booking createBooking(LocalDateTime start, LocalDateTime end) {
        return new Booking(
                "TEST",
                new IndividualUser("test@mail.com", "Test"),
                new Room("Room1", 10, Set.of(), Money.of("60")), // 60 PLN/h
                start,
                end,
                Money.of("0")
        );
    }

    @Test
    void shouldCalculateStandardPrice() {
        StandardPricing pricing = new StandardPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2); // 120 min

        Booking booking = createBooking(start, end);

        Money price = pricing.price(booking);

        assertEquals(Money.of("120.00"), price);
    }

    @Test
    void shouldApplyHappyHoursDiscount() {
        HappyHoursPricing pricing = new HappyHoursPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 14, 0);
        LocalDateTime end = start.plusHours(2);

        Booking booking = createBooking(start, end);

        Money price = pricing.price(booking);

        assertEquals(Money.of("84.00"), price);
    }

    @Test
    void shouldNotApplyDiscountOutsideHappyHours() {
        HappyHoursPricing pricing = new HappyHoursPricing();

        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        Booking booking = createBooking(start, end);

        Money price = pricing.price(booking);

        assertEquals(Money.of("120.00"), price);
    }

    @Test
    void shouldThrowWhenBookingIsNull() {
        StandardPricing pricing = new StandardPricing();

        assertThrows(IllegalArgumentException.class,
                () -> pricing.price(null));
    }
}