package br.com.delivery.domain.exception;

public class InvalidPaymentException extends RuntimeException {
  public InvalidPaymentException(String message) {
    super(message);
  }
}
