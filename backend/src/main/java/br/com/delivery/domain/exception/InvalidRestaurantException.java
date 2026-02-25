package br.com.delivery.domain.exception;

public final class InvalidRestaurantException extends RuntimeException {
  public InvalidRestaurantException(String message) {
    super(message);
  }
}
