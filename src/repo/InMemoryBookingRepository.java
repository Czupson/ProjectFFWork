package repo;

import domain.booking.Booking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBookingRepository implements BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    @Override
    public void add(Booking booking){
        if (booking == null){
            throw new IllegalArgumentException("Booking cannot be null");
        }
        bookings.add(booking);
    }

    @Override
    public Optional<Booking> findById(String id){
        return bookings.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    @Override
    public List<Booking> findAll(){
        return new ArrayList<>(bookings);
    }
}
