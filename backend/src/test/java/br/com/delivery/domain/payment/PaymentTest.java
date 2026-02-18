package br.com.delivery.domain.payment;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;

public class PaymentTest {
  private final Money amount = new Money(BigDecimal.TEN, Currency.BRL);
  private final FakeApprovedPaymentMethod fakeApprovedMethod = new FakeApprovedPaymentMethod();
  // private final FakeRejectedPaymentMethod fakeRejectedMethod = new
  // FakeRejectedPaymentMethod();

  @Test
  void shouldCreateWithPendingStatus() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, amount);
    assertEquals(PaymentStatus.PENDING, payment.getStatus());
  }
}
