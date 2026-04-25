package money;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyFromString() {
        Money money = Money.of("10.123");
        assertEquals(new BigDecimal("10.12"), money.getAmount());
    }

    @Test
    void shouldCreateMoneyFromDouble() {
        Money money = Money.of(10.129);
        assertEquals(new BigDecimal("10.13"), money.getAmount());
    }

    @Test
    void shouldAddMoney() {
        Money m1 = Money.of("10.00");
        Money m2 = Money.of("5.50");

        Money result = m1.add(m2);

        assertEquals(new BigDecimal("15.50"), result.getAmount());
    }

    @Test
    void shouldSubtractMoney() {
        Money m1 = Money.of("10.00");
        Money m2 = Money.of("3.25");

        Money result = m1.subtract(m2);

        assertEquals(new BigDecimal("6.75"), result.getAmount());
    }

    @Test
    void shouldMultiplyByBigDecimal() {
        Money money = Money.of("10.00");

        Money result = money.multiply(new BigDecimal("2.5"));

        assertEquals(new BigDecimal("25.00"), result.getAmount());
    }

    @Test
    void shouldMultiplyByDouble() {
        Money money = Money.of("10.00");

        Money result = money.multiply(1.5);

        assertEquals(new BigDecimal("15.00"), result.getAmount());
    }

    @Test
    void shouldCompareMoney() {
        Money m1 = Money.of("10.00");
        Money m2 = Money.of("20.00");

        assertTrue(m1.compareTo(m2) < 0);
        assertTrue(m2.compareTo(m1) > 0);
        assertEquals(0, m1.compareTo(Money.of("10.00")));
    }

    @Test
    void shouldBeEqualIgnoringScale() {
        Money m1 = Money.of("10.0");
        Money m2 = Money.of("10.00");

        assertEquals(m1, m2);
    }

    @Test
    void shouldReturnProperToString() {
        Money money = Money.of("10.00");

        assertEquals("10.00 PLN", money.toString());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Money(null));
    }

    @Test
    void shouldThrowExceptionWhenAddingNull() {
        Money money = Money.of("10.00");

        assertThrows(IllegalArgumentException.class, () -> money.add(null));
    }

    @Test
    void shouldThrowExceptionWhenSubtractingNull() {
        Money money = Money.of("10.00");

        assertThrows(IllegalArgumentException.class, () -> money.subtract(null));
    }

    @Test
    void shouldThrowExceptionWhenComparingNull() {
        Money money = Money.of("10.00");

        assertThrows(IllegalArgumentException.class, () -> money.compareTo(null));
    }

    @Test
    void shouldThrowExceptionWhenMultiplierIsNull() {
        Money money = Money.of("10.00");

        assertThrows(IllegalArgumentException.class, () -> money.multiply((BigDecimal) null));
    }
}