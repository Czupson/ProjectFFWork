package domain.resource;

import money.Money;

public abstract class Resource {
    private final String name;
    private final Money customHourlyRate;

    protected Resource(String name, Money customHourlyRate) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.customHourlyRate = customHourlyRate;
    }

    public String getName() {
        return name;
    }

    public Money getCustomHourlyRate() {
        return customHourlyRate;
    }

    protected abstract Money baseRatePerHour();

    public abstract String describe();

    public Money hourlyRate() {
        return (customHourlyRate != null) ? customHourlyRate : baseRatePerHour();
    }

    @Override
    public String toString() {
        return describe() + " | rate=" + hourlyRate();
    }
}
