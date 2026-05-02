package service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Room;
import domain.user.IndividualUser;
import money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import payment.Payment;
import payment.PaymentStatus;
import pricing.StandardPricing;
import repo.InMemoryBookingRepository;
import repo.InMemoryResourceRepository;
import repo.InMemoryUserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    private BookingService bookingService;
    private PaymentService paymentService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        var bookingRepo = new InMemoryBookingRepository();
        bookingService = new BookingService(
                bookingRepo,
                new StandardPricing()
        );

        paymentService = new PaymentService(bookingRepo);

        var user = new IndividualUser("test@mail.com", "Test User");
        var room = new Room("Room1", 10, Set.of("projector"), Money.of("80"));

        booking = bookingService.book(
                user,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2)
        );

        bookingService.confirm(booking.getId());
    }


    @Test
    void shouldPayBooking() {
        Payment payment = paymentService.pay(booking.getId(), "1234");

        assertNotNull(payment);
        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
        assertEquals(payment, booking.getPayment());
    }

    @Test
    void shouldThrowWhenBookingNotConfirmed() {
        var user = new IndividualUser("a@mail.com", "User");
        var room = new Room("Room2", 10, Set.of(), Money.of("80"));

        Booking notConfirmed = bookingService.book(
                user,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1)
        );

        assertThrows(IllegalStateException.class,
                () -> paymentService.pay(notConfirmed.getId(), "1234"));
    }

    @Test
    void shouldThrowWhenBookingAlreadyPaid() {
        paymentService.pay(booking.getId(), "1234");

        assertThrows(IllegalStateException.class,
                () -> paymentService.pay(booking.getId(), "1234"));
    }

    @Test
    void shouldThrowWhenInvalidCard() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.pay(booking.getId(), "12"));
    }

    @Test
    void shouldThrowWhenBookingNotFound() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.pay("INVALID", "1234"));
    }

    @Test
    void shouldPayWithWallet() {
        Payment payment = paymentService.payWithWallet(booking.getId());

        assertEquals(PaymentStatus.CAPTURED, payment.getStatus());
        assertEquals(payment, booking.getPayment());
    }

    @Test
    void shouldRefundWalletPayment() {
        paymentService.payWithWallet(booking.getId());

        paymentService.refund(booking.getId());

        assertEquals(PaymentStatus.REFUNDED, booking.getPayment().getStatus());
    }
}