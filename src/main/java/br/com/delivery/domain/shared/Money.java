package br.com.delivery.domain.shared;

import java.util.Objects;
import java.math.BigDecimal;

public final class Money {
  private final BigDecimal amount;
  private final Currency currency;

  private Money(BigDecimal amount, Currency currency) {
    this.amount = Objects.requireNonNull(amount);
    this.currency = Objects.requireNonNull(currency);
  }

  public static Money of(BigDecimal amount, Currency currency) {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(currency);
    return new Money(amount, currency);
  }

  public static Money zero(Currency currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  public Money add(Money value) {
    this.ensureSameCurrency(value);
    return new Money(this.amount.add(value.amount), this.currency);
  }

  public Money multiply(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("O valor não pode ser zero.");
    }
    return new Money(this.amount.multiply(BigDecimal.valueOf(value)), this.currency);
  }

  public boolean isPositive() {
    return amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean isNegative() {
    return amount.compareTo(BigDecimal.ZERO) < 0;
  }

  public boolean isZero() {
    return amount.compareTo(BigDecimal.ZERO) == 0;
  }

  private void ensureSameCurrency(Money value) {
    if (!this.currency.equals(value.currency)) {
      throw new IllegalArgumentException("Operações não podem ser feitas entre moedas diferentes.");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Money)) {
      return false;
    }
    Money money = (Money) obj;
    return this.amount.equals(money.amount) && this.currency == money.currency;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    return this.currency + " " + this.amount;
  }
}
