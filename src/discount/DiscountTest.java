package discount;

import domain.booking.Booking;
import domain.resource.Room;
import domain.user.CompanyUser;
import domain.user.IndividualUser;
import domain.user.User;
import money.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {
    private static final Money BASE_PRICE = Money.of("100");
    private static final Money STUDENT_PRICE = Money.of("80.00");
    private static final Money COMPANY_PRICE = Money.of("90.00");

    private static final String TEST_ID = "TEST";
    private static final String COMPANY_ID = "123";

    private Booking createBooking(User user) {
        return new Booking(
                TEST_ID,
                user,
                new Room("Room", 10, Set.of(), BASE_PRICE),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                BASE_PRICE
        );
    }

    @Test
    void shouldApplyStudentDiscount() {
        var discount = new StudentDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(BASE_PRICE, booking);

        assertEquals(STUDENT_PRICE, result);
    }

    @Test
    void shouldApplyCompanyDiscount() {
        var discount = new CompanyTierDiscount();

        var user = new CompanyUser("c@mail.com", "Jan Kowalski", "Company", COMPANY_ID);
        var booking = createBooking(user);

        Money result = discount.apply(BASE_PRICE, booking);

        assertEquals(COMPANY_PRICE, result);
    }

    @Test
    void shouldNotApplyDiscountForWrongUserType() {
        var discount = new CompanyTierDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(BASE_PRICE, booking);

        assertEquals(BASE_PRICE, result);
    }

    @Test
    void shouldNotApplyDiscountWhenNoDiscount() {
        var discount = new NoDiscount();

        var user = new IndividualUser("a@mail.com", "User");
        var booking = createBooking(user);

        Money result = discount.apply(BASE_PRICE, booking);

        assertEquals(BASE_PRICE, result);
    }
}
