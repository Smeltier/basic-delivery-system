package br.com.delivery.domain.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

public class PaymentIdTest {
  @Test
  void shouldCreatePaymentWithValidUUID() {
    UUID uuid = UUID.randomUUID();
    PaymentId paymentId = new PaymentId(uuid);

    assertEquals(uuid, paymentId.value());
  }

  @Test
  void shouldThrowExceptionWhenUUIDIsNull() {
    assertThrows(NullPointerException.class,
        () -> new PaymentId(null));
  }

  @Test
  void generateShouldReturnNonNullValue() {
    PaymentId paymentId = PaymentId.generate();

    assertNotNull(paymentId);
    assertNotNull(paymentId.value());
  }

  @Test
  void generateShouldCreateDifferentIds() {
    PaymentId id1 = PaymentId.generate();
    PaymentId id2 = PaymentId.generate();

    assertNotEquals(id1, id2);
  }

  @Test
  void twoPaymentsWithSameUUIDNeedToBeEquals() {
    UUID id = UUID.randomUUID();
    PaymentId id1 = new PaymentId(id);
    PaymentId id2 = new PaymentId(id);

    assertEquals(id1, id2);
  }
}
