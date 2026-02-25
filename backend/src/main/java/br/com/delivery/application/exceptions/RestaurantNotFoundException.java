package br.com.delivery.application.exceptions;

public class RestaurantNotFoundException extends RuntimeException {
  public RestaurantNotFoundException(String message) {
    super(message);
  }
}