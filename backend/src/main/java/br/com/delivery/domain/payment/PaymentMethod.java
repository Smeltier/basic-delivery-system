package br.com.delivery.domain.payment;

public interface PaymentMethod {
  PaymentProcessingResult process(Payment payment);
}
