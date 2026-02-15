package br.com.delivery.domain.shared;

import java.util.Objects;
import java.math.BigDecimal;

import br.com.delivery.domain.exception.InvalidMoneyOperationException;
import br.com.delivery.domain.exception.CurrencyMismatchException;

public record Money(BigDecimal amount, Currency currency) {
  public Money {
    amount = Objects.requireNonNull(amount);
    currency = Objects.requireNonNull(currency);
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
      throw new InvalidMoneyOperationException("O valor não pode ser zero.");
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
      throw new CurrencyMismatchException("Operações não podem ser feitas entre moedas diferentes.");
    }
  }
}
