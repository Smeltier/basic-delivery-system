package br.com.delivery.domain.exception;

public class InvalidClientException extends RuntimeException {
  public InvalidClientException(String message) {
    super(message);
  }
}
