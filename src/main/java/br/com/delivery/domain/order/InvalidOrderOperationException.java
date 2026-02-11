package br.com.delivery.domain.order;

public class InvalidOrderOperationException extends RuntimeException {
  public InvalidOrderOperationException(String message) {
    super(message);
  }
}
