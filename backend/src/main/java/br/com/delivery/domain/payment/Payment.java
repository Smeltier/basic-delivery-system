package br.com.delivery.domain.payment;

import java.util.Objects;

import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.exception.InvalidPaymentOperationException;

public final class Payment {
  private final PaymentId id;
  private final OrderId orderId;
  private final IPaymentMethod paymentMethod;
  private final Money amount;
  private PaymentStatus status;

  private Payment(PaymentId id, OrderId orderId, IPaymentMethod paymentMethod, Money amount) {
    this.id = Objects.requireNonNull(id);
    this.orderId = Objects.requireNonNull(orderId);
    this.paymentMethod = Objects.requireNonNull(paymentMethod);
    this.amount = Objects.requireNonNull(amount);
    this.status = PaymentStatus.PENDING;
  }

  public static Payment create(OrderId orderId, IPaymentMethod paymentMethod, Money amount) {
    return new Payment(PaymentId.generate(), orderId, paymentMethod, amount);
  }

  public void process() {
    if (status != PaymentStatus.PENDING) {
      throw new InvalidPaymentOperationException("O pagamento não pode ser processado no estado " + status);
    }

    PaymentProcessingResult result = paymentMethod.process(this);

    switch (result) {
      case APPROVED -> changeStatus(PaymentStatus.APPROVED);
      case REJECTED -> changeStatus(PaymentStatus.DECLINED);
      case PENDING -> changeStatus(PaymentStatus.PENDING);
    }
  }

  public void cancel() {
    if (status != PaymentStatus.PENDING) {
      throw new InvalidPaymentOperationException("Só pagamentos pendentes podem ser cancelados.");
    }
    changeStatus(PaymentStatus.CANCELLED);
  }

  public void refund() {
    if (status != PaymentStatus.APPROVED) {
      throw new InvalidPaymentOperationException("Só pagamentos aprovados podem ser reembolsados.");
    }
    changeStatus(PaymentStatus.REFUNDED);
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

  private void changeStatus(PaymentStatus newStatus) {
    this.status = newStatus;
  }
}
