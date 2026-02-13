package br.com.delivery.domain.payment;

import java.util.Objects;
import java.util.UUID;

public record PaymentId(UUID value) {
  public PaymentId {
    Objects.requireNonNull(value);
  }

  public static PaymentId generate() {
    return new PaymentId(UUID.randomUUID());
  }
}
