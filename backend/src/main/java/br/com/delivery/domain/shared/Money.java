package br.com.delivery.domain.shared;

import java.util.Objects;
import java.math.BigDecimal;

import br.com.delivery.domain.exception.InvalidMoneyOperationException;
import br.com.delivery.domain.exception.CurrencyMismatchException;
import br.com.delivery.domain.exception.InsufficientFundsException;

public record Money(BigDecimal amount, Currency currency) {
  public Money {
    if (isNegative(amount)) {
      throw new InvalidMoneyOperationException("A quantiade não pode ser negativa.");
    }
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

  public Money subtract(Money value) {
    if (!isGreaterThanOrEqual(value)) {
      throw new InsufficientFundsException("O valor passado deve ser maior ou igual ao valor atual.");
    }
    this.ensureSameCurrency(value);
    return new Money(this.amount.subtract(value.amount), this.currency);
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

  public boolean isZero() {
    return amount.compareTo(BigDecimal.ZERO) == 0;
  }

  public boolean isGreaterThanOrEqual(Money value) {
    return this.amount.compareTo(value.amount) >= 0;
  }

  private boolean isNegative(BigDecimal amount) {
    return amount.compareTo(BigDecimal.ZERO) < 0;
  }

  private void ensureSameCurrency(Money value) {
    if (!this.currency.equals(value.currency)) {
      throw new CurrencyMismatchException("Operações não podem ser feitas entre moedas diferentes.");
    }
  }
}
