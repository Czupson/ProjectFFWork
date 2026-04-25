package domain.resource;

import money.Money;

public class Desk extends Resource {
    public enum DeskType { HOT, FIXED }
    private final DeskType type;

    public Desk(String name, DeskType type, Money customRate) {
        super(name, customRate);

        if (type == null) {
            throw new NullPointerException("type is null");
        }

        this.type = type;
    }

    public DeskType getType() {
        return type;
    }

    @Override
    protected Money baseRatePerHour() {
        return (type == DeskType.HOT) ? Money.of(25) : Money.of(40);
    }

    @Override
    public String describe() {
        return "Desk: " + getName() + " | type=" + type;
    }
}
