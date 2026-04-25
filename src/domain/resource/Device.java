package domain.resource;

import money.Money;

public class Device extends Resource {
    private final int quantity;

    public Device(String name, int quantity, Money customRate) {
        super(name, customRate);

        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    @Override
    protected Money baseRatePerHour(){
        return Money.of(40);
    }

    @Override
    public String describe() {
        return "Device: " + getName() + " | quantity: " + quantity;
    }
}
