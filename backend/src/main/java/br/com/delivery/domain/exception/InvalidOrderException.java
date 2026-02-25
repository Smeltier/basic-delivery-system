package br.com.delivery.domain.exception;

public class InvalidOrderException extends RuntimeException {
  public InvalidOrderException(String message) {
    super(message);
  }
}
