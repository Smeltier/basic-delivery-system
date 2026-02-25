package br.com.delivery.domain.exception;

public class InvalidOrderItemException extends RuntimeException {
  public InvalidOrderItemException(String message) {
    super(message);
  }
}
