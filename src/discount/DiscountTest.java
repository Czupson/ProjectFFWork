package discount;

import domain.booking.Booking;
import domain.resource.Room;
import domain.user.CompanyUser;
import domain.user.IndividualUser;
import money.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    private Booking createBooking(Object user) {
        return new Booking(
                "TEST",
                (domain.user.User) user,
                new Room("Room", 10, Set.of(), Money.of("100")),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                Money.of("100")
        );
    }

    @Test
    void shouldApplyStudentDiscount() {
        var discount = new StudentDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(Money.of("100"), booking);

        assertEquals(Money.of("80.00"), result);
    }

    @Test
    void shouldApplyCompanyDiscount() {
        var discount = new CompanyTierDiscount();

        var user = new CompanyUser("c@mail.com", "Company", "123");
        var booking = createBooking(user);

        Money result = discount.apply(Money.of("100"), booking);

        assertEquals(Money.of("90.00"), result);
    }

    @Test
    void shouldNotApplyDiscountForWrongUserType() {
        var discount = new CompanyTierDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(Money.of("100"), booking);

        assertEquals(Money.of("100.00"), result);
    }

    @Test
    void shouldNotApplyDiscountWhenNoDiscount() {
        var discount = new NoDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(Money.of("100"), booking);

        assertEquals(Money.of("100.00"), result);
    }
}