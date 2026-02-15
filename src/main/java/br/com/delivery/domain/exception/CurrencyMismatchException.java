package br.com.delivery.domain.exception;

public class CurrencyMismatchException extends RuntimeException {
  public CurrencyMismatchException(String message) {
    super(message);
  }
}
