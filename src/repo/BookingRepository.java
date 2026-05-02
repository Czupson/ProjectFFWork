package repo;

import domain.booking.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void add(Booking booking);
    Optional<Booking> findById(String id);
    List<Booking> findAll();
}
