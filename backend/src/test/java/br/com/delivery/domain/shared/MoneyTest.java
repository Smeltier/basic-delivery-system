package br.com.delivery.domain.shared;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.delivery.domain.exception.InsufficientFundsException;
import br.com.delivery.domain.exception.InvalidMoneyException;
import br.com.delivery.domain.exception.CurrencyMismatchException;

public class MoneyTest {
  @Test
  void shouldThrowWhenValueIsNegative() {
    assertThrows(InvalidMoneyException.class,
        () -> new Money(new BigDecimal("-10"), Currency.BRL));
  }

  @Test
  void shouldThrowWithNullCurrency() {
    assertThrows(NullPointerException.class,
        () -> new Money(BigDecimal.ONE, null));
  }

  @Test
  void shouldThrowWithNullAmount() {
    assertThrows(NullPointerException.class,
        () -> new Money(null, Currency.BRL));
  }

  @Test
  void moneysWithSameValuesShouldEquals() {
    var mn1 = new Money(BigDecimal.TEN, Currency.BRL);
    var mn2 = new Money(BigDecimal.TEN, Currency.BRL);

    assertEquals(mn1, mn2);
  }

  @Test
  void moneyWithPositiveValueShouldBePositive() {
    var money = new Money(BigDecimal.TEN, Currency.BRL);

    assertTrue(money.isPositive());
  }

  // @Test
  // void moneyWithNegativeValueShouldBeNegative() {
  // var money = new Money(new BigDecimal("-10.0"), Currency.BRL);
  //
  // assertTrue(money.isNegative());
  // }

  @Test
  void multiplyShouldReturnCorrectValue() {
    var value = BigDecimal.ONE;

    var multiply = value.multiply(new BigDecimal(50));
    var mn1 = new Money(multiply, Currency.BRL);

    var mn2 = new Money(value, Currency.BRL);
    Money mn3 = mn2.multiply(50);

    assertEquals(mn1, mn3);
  }

  @Test
  void differentCurrencyShouldThrowOnAdd() {
    var money1 = new Money(BigDecimal.ONE, Currency.BRL);
    var money2 = new Money(BigDecimal.ONE, Currency.CAD);

    assertThrows(CurrencyMismatchException.class,
        () -> money1.add(money2));
  }

  @Test
  void shouldThrowWhenSubtractionResultIsNegative() {
    Money mn1 = new Money(BigDecimal.ONE, Currency.BRL);
    Money mn2 = new Money(BigDecimal.TEN, Currency.BRL);

    assertThrows(InsufficientFundsException.class,
        () -> mn1.subtract(mn2));
  }
}
