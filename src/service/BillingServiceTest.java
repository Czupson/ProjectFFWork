package service;

import billing.Invoice;
import billing.SimpleBillingService;
import domain.booking.Booking;
import domain.resource.Room;
import domain.user.IndividualUser;
import money.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pricing.StandardPricing;
import repo.InMemoryBookingRepository;
import repo.InMemoryResourceRepository;
import repo.InMemoryUserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BillingServiceTest {

    private BookingService bookingService;
    private PaymentService paymentService;
    private SimpleBillingService billingService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        var bookingRepo = new InMemoryBookingRepository();

        bookingService = new BookingService(
                bookingRepo,
                new StandardPricing()
        );

        paymentService = new PaymentService(bookingRepo);
        billingService = new SimpleBillingService();

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
    void shouldCreateInvoice() {
        paymentService.pay(booking.getId(), "1234");

        Invoice invoice = billingService.toInvoice(booking);

        assertNotNull(invoice);
        assertEquals(booking.getUser(), invoice.getBuyer());
        assertEquals(booking.getCalculatedPrice(), invoice.getTotal());
        assertTrue(invoice.getInvoiceNumber().startsWith("INV-"));
    }

    @Test
    void shouldThrowWhenBookingNotPaid() {
        assertThrows(IllegalStateException.class,
                () -> billingService.toInvoice(booking));
    }

    @Test
    void shouldGenerateProperInvoiceNumberFormat() {
        paymentService.pay(booking.getId(), "1234");

        Invoice invoice = billingService.toInvoice(booking);

        assertTrue(invoice.getInvoiceNumber().matches("INV-\\d{8}-\\d+"));
    }

}
