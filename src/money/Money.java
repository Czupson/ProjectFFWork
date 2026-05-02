package money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money implements Comparable<Money> {
    private static final String CURRENCY = "PLN";

    private final BigDecimal amount;

    public Money(final BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_UP);

        if (normalized.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = normalized;
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal multiplier) {
        Objects.requireNonNull(multiplier, "Multiplier cannot be null");
        return new Money(amount.multiply(multiplier));
    }

    public Money multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }

    @Override
    public int compareTo(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return this.amount.compareTo(other.amount);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return amount + " " + CURRENCY;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Money)) return false;
        Money money = (Money) object;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }
}
