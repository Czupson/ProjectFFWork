package domain.resource;

import money.Money;

public class Desk extends Resource {

    public enum DeskType { HOT, FIXED }

    private static final Money HOT_RATE = Money.of(25);
    private static final Money FIXED_RATE = Money.of(40);

    private final DeskType type;

    public Desk(String name, DeskType type, Money customRate) {
        super(name, customRate);

        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        this.type = type;
    }

    public DeskType getType() {
        return type;
    }

    @Override
    protected Money baseRatePerHour() {
        return (type == DeskType.HOT) ? HOT_RATE : FIXED_RATE;
    }

    @Override
    public String describe() {
        return "Desk: " + getName() + " | type=" + type;
    }
}
