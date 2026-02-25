package br.com.delivery.domain.payment;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.exception.InvalidPaymentException;

public class PaymentTest {
  private final Money amount = new Money(BigDecimal.TEN, Currency.BRL);
  private final FakeApprovedPaymentMethod fakeApprovedMethod = new FakeApprovedPaymentMethod();
  private final FakeRejectedPaymentMethod fakeRejectedMethod = new FakeRejectedPaymentMethod();

  @Test
  void shouldCreateWithPendingStatus() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, amount);
    assertEquals(PaymentStatus.PENDING, payment.getStatus());
  }

  @Test
  void shouldThrowWhenCancelAProcessedPayment() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, this.amount);
    payment.process();

    assertThrows(InvalidPaymentException.class,
        () -> payment.cancel());
  }

  @Test
  void shouldThrowWhenProcessAProcessedPayment() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, this.amount);
    payment.process();

    assertThrows(InvalidPaymentException.class,
        () -> payment.process());
  }

  @Test
  void shouldThrowWhenRefundAUnapprovedPayment() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeRejectedMethod, this.amount);
    payment.process();

    assertThrows(InvalidPaymentException.class,
        () -> payment.refund());
  }

  @Test
  void statusShouldBeApprovedWhenPaymentIsApproved() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, this.amount);
    payment.process();

    assertEquals(PaymentStatus.APPROVED, payment.getStatus());
  }

  @Test
  void statusShouldBeDeclinedWhenPaymentIsRejected() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeRejectedMethod, this.amount);
    payment.process();

    assertEquals(PaymentStatus.DECLINED, payment.getStatus());
  }

  @Test
  void shouldCancelPendingPayment() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeRejectedMethod, this.amount);
    payment.cancel();

    assertEquals(PaymentStatus.CANCELLED, payment.getStatus());
  }

  @Test
  void shouldRefundApprovedPayment() {
    Payment payment = Payment.create(OrderId.generate(), this.fakeApprovedMethod, this.amount);
    payment.process();
    payment.refund();

    assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
  }
}
