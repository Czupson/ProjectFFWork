package service;

import domain.booking.Booking;
import domain.booking.BookingStatus;
import domain.resource.Device;
import domain.resource.Resource;
import domain.user.User;
import discount.Discountable;
import discount.NoDiscount;
import money.Money;
import pricing.PricingPolicy;
import repo.BookingRepository;
import repo.ResourceRepository;
import repo.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookingService {

    private final UserRepository userRepo;
    private final ResourceRepository resourceRepo;
    private final BookingRepository bookingRepo;
    private final PricingPolicy pricingPolicy;

    private Discountable discount = new NoDiscount();

    private final AtomicInteger counter = new AtomicInteger(1);

    public BookingService(
            UserRepository userRepo,
            ResourceRepository resourceRepo,
            BookingRepository bookingRepo,
            PricingPolicy policy
    ) {
        this.userRepo = userRepo;
        this.resourceRepo = resourceRepo;
        this.bookingRepo = bookingRepo;
        this.pricingPolicy = policy;
    }

    public void setDiscount(Discountable discount) {
        this.discount = discount;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, LocalDateTime end) {

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("End must be after start");
        }

        checkConflicts(resource, start, end);

        Booking temp = new Booking("TEMP", user, resource, start, end, Money.of("0"));

        Money base = pricingPolicy.price(temp);
        Money finalPrice = discount.apply(base, temp);

        String id = generateId(start);

        Booking booking = new Booking(id, user, resource, start, end, finalPrice);

        bookingRepo.add(booking);
        return booking;
    }

    public Booking book(User user, Resource resource, LocalDateTime start, int durationMinutes) {
        return book(user, resource, start, start.plusMinutes(durationMinutes));
    }

    private void checkConflicts(Resource resource, LocalDateTime start, LocalDateTime end) {

        List<Booking> conflicts = bookingRepo.findAll().stream()
                .filter(b -> b.getResource().equals(resource))
                .filter(b -> b.getStatus() == BookingStatus.PENDING
                        || b.getStatus() == BookingStatus.CONFIRMED)
                .filter(b -> overlaps(start, end, b.getStart(), b.getEnd()))
                .collect(Collectors.toList());

        if (resource instanceof Device device) {
            if (conflicts.size() >= device.getQuantity()) {
                throw new IllegalStateException("No available devices");
            }
        } else {
            if (!conflicts.isEmpty()) {
                throw new IllegalStateException("Resource already booked");
            }
        }
    }

    private boolean overlaps(LocalDateTime aStart, LocalDateTime aEnd,
                             LocalDateTime bStart, LocalDateTime bEnd) {
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private String generateId(LocalDateTime start) {
        String date = String.format("%04d%02d%02d",
                start.getYear(),
                start.getMonthValue(),
                start.getDayOfMonth());

        return "BK-" + date + "-" + counter.getAndIncrement();
    }

    public void confirm(String bookingId) {
        find(bookingId).confirm();
    }

    public void cancel(String bookingId) {
        find(bookingId).cancel();
    }

    public void complete(String bookingId) {
        find(bookingId).complete();
    }

    public List<Booking> list() {
        return bookingRepo.findAll();
    }

    private Booking find(String id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }
}