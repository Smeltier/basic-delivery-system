package br.com.delivery.domain.exception;

public class InvalidMoneyException extends RuntimeException {
  public InvalidMoneyException(String message) {
    super(message);
  }
}
