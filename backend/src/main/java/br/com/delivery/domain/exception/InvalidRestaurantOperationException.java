package br.com.delivery.domain.exception;

public final class InvalidRestaurantOperationException extends RuntimeException {
  public InvalidRestaurantOperationException(String message) {
    super(message);
  }
}
