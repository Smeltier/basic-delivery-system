package br.com.delivery.application.exceptions;

public class MenuItemNotFoundException extends RuntimeException {
  public MenuItemNotFoundException(String message) {
    super(message);
  }
}