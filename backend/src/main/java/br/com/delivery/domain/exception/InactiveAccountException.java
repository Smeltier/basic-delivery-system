package br.com.delivery.domain.exception;

public class InactiveAccountException extends RuntimeException {
  public InactiveAccountException(String message) {
    super(message);
  }
}
