package br.com.delivery.domain.exception;

public class InactiveItemException extends RuntimeException {
  public InactiveItemException(String message) {
    super(message);
  }
}
