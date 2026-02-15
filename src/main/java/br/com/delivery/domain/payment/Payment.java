package br.com.delivery.domain.payment;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.shared.Money;

import java.util.Objects;

public class Payment {
  private final PaymentId id;
  private final OrderId orderId;
  private final PaymentMethod paymentMethod;
  private final Money amount;
  private PaymentStatus status;

  private Payment(PaymentId id, OrderId orderId, PaymentMethod paymentMethod, Money amount) {
    this.id = Objects.requireNonNull(id);
    this.orderId = Objects.requireNonNull(orderId);
    this.paymentMethod = Objects.requireNonNull(paymentMethod);
    this.amount = Objects.requireNonNull(amount);
    this.status = PaymentStatus.PENDING;
  }

  public static Payment create(OrderId orderId, PaymentMethod paymentMethod, Money amount) {
    return new Payment(PaymentId.generate(), orderId, paymentMethod, amount);
  }

  public void process() {
    if (status != PaymentStatus.PENDING) {
      throw new IllegalStateException("O pagamento não pode ser processado no estado " + status);
    }

    PaymentProcessingResult result = paymentMethod.process(this);

    switch (result) {
      case APPROVED -> status = PaymentStatus.APPROVED;
      case REJECTED -> status = PaymentStatus.DECLINED;
      case PENDING -> status = PaymentStatus.PENDING;
    }
  }

  public void cancel() {
    if (status != PaymentStatus.PENDING) {
      throw new IllegalStateException("Só pagamentos pendentes podem ser processados.");
    }
    this.status = PaymentStatus.CANCELLED;
  }

  public void refund() {
    if (status != PaymentStatus.APPROVED) {
      throw new IllegalStateException("Só pagamentos aprovados podem ser reembolsados.");
    }
    this.status = PaymentStatus.REFUNDED;
  }

  public boolean isApproved() {
    return this.status == PaymentStatus.APPROVED;
  }

  public PaymentId getId() {
    return id;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public Money getAmount() {
    return amount;
  }

  public PaymentStatus getStatus() {
    return status;
  }
}
