package br.com.delivery.domain.exception;

public class InvalidOrderOperationException extends RuntimeException {
  public InvalidOrderOperationException(String message) {
    super(message);
  }
}
