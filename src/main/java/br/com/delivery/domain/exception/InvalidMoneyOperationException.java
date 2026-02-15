package br.com.delivery.domain.exception;

public class InvalidMoneyOperationException extends RuntimeException {
  public InvalidMoneyOperationException(String message) {
    super(message);
  }
}
