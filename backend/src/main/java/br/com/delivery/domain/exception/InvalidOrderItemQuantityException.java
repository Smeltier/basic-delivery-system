package br.com.delivery.domain.exception;

public class InvalidOrderItemQuantityException extends RuntimeException {
  public InvalidOrderItemQuantityException(String message) {
    super(message);
  }
}
