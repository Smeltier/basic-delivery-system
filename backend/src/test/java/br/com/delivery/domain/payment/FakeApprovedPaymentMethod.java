package br.com.delivery.domain.payment;

public class FakeApprovedPaymentMethod implements PaymentMethod {
  public PaymentProcessingResult process(Payment payment) {
    return PaymentProcessingResult.APPROVED;
  }
}
