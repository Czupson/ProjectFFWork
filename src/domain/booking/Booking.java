package domain.booking;

import domain.resource.Resource;
import domain.user.User;
import money.Money;
import payment.Payment;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Booking {
    private final String id;
    private final User user;
    private final Resource resource;
    private final LocalDateTime start;
    private final LocalDateTime end;

    private BookingStatus status;
    private Money calculatedPrice;
    private Payment payment;

    public Booking(String id, User user, Resource resource, LocalDateTime start, LocalDateTime end, Money calculatedPrice) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id cannot be empty");
        if (user == null)
            throw new IllegalArgumentException("user cannot be null");
        if (resource == null)
            throw new IllegalArgumentException("resource cannot be null");
        if (start == null || end == null)
            throw new IllegalArgumentException("start and end cannot be null");

        if (!start.isBefore(end))
            throw new IllegalArgumentException("start must be before end");

        this.id = id;
        this.user = user;
        this.resource = resource;
        this.start = start;
        this.end = end;
        this.calculatedPrice = calculatedPrice;
        this.status = BookingStatus.PENDING;
        this.payment = null;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Resource getResource() {
        return resource;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Money getCalculatedPrice() {
        return calculatedPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void confirm() {
        if (status != BookingStatus.PENDING) {
            throw new IllegalStateException("Only PENDING booking can be confirmed");
        }
        status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == BookingStatus.CANCELLED || status == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel booking in status: " + status);
        }
        status = BookingStatus.CANCELLED;
    }

    public void complete() {
        if (status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED booking can be completed");
        }
        status = BookingStatus.COMPLETED;
    }

    public void attachPayment(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("payment cannot be null");
        }
        this.payment = payment;
    }

    public int durationMinutes() {
        return (int) ChronoUnit.MINUTES.between(start, end);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", user=" + user.getDisplayName() +
                ", resource=" + resource.getName() +
                ", start=" + start +
                ", end=" + end +
                ", status=" + status +
                ", price=" + calculatedPrice +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Booking)) return false;
        Booking booking = (Booking) object;
        return id.equals(booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}