package br.com.delivery.domain.payment;

import java.util.UUID;

public record PaymentId(UUID value) {
  public PaymentId {
    if (value == null) {
      throw new IllegalArgumentException("O valor não pode ser nulo");
    }
  }

  public static PaymentId generate() {
    return new PaymentId(UUID.randomUUID());
  }
}
