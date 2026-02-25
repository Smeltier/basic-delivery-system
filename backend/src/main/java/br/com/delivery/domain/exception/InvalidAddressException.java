package br.com.delivery.domain.exception;

public class InvalidAddressException extends RuntimeException {
  public InvalidAddressException(String message) {
    super(message);
  }
}
