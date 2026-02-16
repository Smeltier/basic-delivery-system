package br.com.delivery.domain.payment;

public class FakeRejectedPaymentMethod implements PaymentMethod {
  public PaymentProcessingResult process(Payment payment) {
    return PaymentProcessingResult.REJECTED;
  }
}
