package domain.resource;

import money.Money;

import java.util.HashSet;
import java.util.Set;

public class Room extends Resource {

    private static final Money DEFAULT_HOURLY_RATE = Money.of("80");

    private final int seats;
    private final Set<String> equipment;

    public Room(String name, int seats, Set<String> equipment, Money customRate) {
        super(name, customRate);

        if (seats <= 0) {
            throw new IllegalArgumentException("seats must be greater than 0");
        }

        this.seats = seats;
        this.equipment = (equipment != null) ? new HashSet<>(equipment) : new HashSet<>();
    }

    public int getSeats() {
        return seats;
    }

    public Set<String> getEquipment() {
        return new HashSet<>(equipment);
    }

    @Override
    protected Money baseRatePerHour() {
        return DEFAULT_HOURLY_RATE;
    }

    @Override
    public String describe() {
        return "Room: " + getName() + " | seats=" + seats + " | equipment=" + equipment;
    }
}
