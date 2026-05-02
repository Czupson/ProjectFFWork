package service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Desk;
import domain.resource.Device;
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

class BookingServiceTest {

    private BookingService service;

    private IndividualUser user;
    private Room room;
    private Desk desk;
    private Device device;

    @BeforeEach
    void setUp() {
        service = new BookingService(
                new InMemoryBookingRepository(),
                new StandardPricing()
        );

        user = new IndividualUser("test@mail.com", "Test User");

        room = new Room("Room1", 10, Set.of("projector"), Money.of("80"));
        desk = new Desk("Desk1", Desk.DeskType.HOT, Money.of("25"));
        device = new Device("Device1", 2, Money.of("40"));
    }

    @Test
    void shouldCreateBooking() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        Booking booking = service.book(user, room, start, end);

        assertNotNull(booking);
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(user, booking.getUser());
        assertEquals(room, booking.getResource());
    }

    @Test
    void shouldCreateBookingUsingDuration() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);

        Booking booking = service.book(user, room, start, 60);

        assertEquals(60, booking.durationMinutes());
    }

    @Test
    void shouldThrowWhenEndBeforeStart() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusMinutes(10);

        assertThrows(IllegalArgumentException.class,
                () -> service.book(user, room, start, end));
    }

    @Test
    void shouldThrowWhenBookingConflicts() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        service.book(user, room, start, end);

        assertThrows(IllegalStateException.class,
                () -> service.book(user, room, start.plusMinutes(30), end.plusMinutes(30)));
    }

    @Test
    void shouldAllowBookingDifferentResources() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(2);

        service.book(user, room, start, end);

        Booking second = service.book(user, desk, start, end);

        assertNotNull(second);
    }

    @Test
    void shouldAllowMultipleDeviceBookingsUpToQuantity() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 15, 10, 0);
        LocalDateTime end = start.plusHours(1);

        service.book(user, device, start, end);
        service.book(user, device, start, end);

        assertThrows(IllegalStateException.class,
                () -> service.book(user, device, start, end));
    }

    @Test
    void shouldConfirmBooking() {
        Booking booking = service.book(user, room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        service.confirm(booking.getId());

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void shouldCancelBooking() {
        Booking booking = service.book(user, room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        service.cancel(booking.getId());

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void shouldCompleteBooking() {
        Booking booking = service.book(user, room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        service.confirm(booking.getId());
        service.complete(booking.getId());

        assertEquals(BookingStatus.COMPLETED, booking.getStatus());
    }

    @Test
    void shouldThrowWhenCompletingNotConfirmed() {
        Booking booking = service.book(user, room,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));

        assertThrows(IllegalStateException.class,
                () -> service.complete(booking.getId()));
    }
}