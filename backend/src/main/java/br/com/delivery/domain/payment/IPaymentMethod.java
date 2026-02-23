package br.com.delivery.domain.payment;

public interface IPaymentMethod {
  PaymentProcessingResult process(Payment payment);
}