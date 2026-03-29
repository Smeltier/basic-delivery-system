package br.com.delivery.infrastructure.web.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.delivery.application.exceptions.AccountNotFoundException;
import br.com.delivery.application.exceptions.ClientNotFoundException;
import br.com.delivery.application.exceptions.MenuItemNotFoundException;
import br.com.delivery.application.exceptions.OrderNotFoundException;
import br.com.delivery.application.exceptions.RestaurantNotFoundException;
import br.com.delivery.domain.exception.CurrencyMismatchException;
import br.com.delivery.domain.exception.InactiveAccountException;
import br.com.delivery.domain.exception.InactiveItemException;
import br.com.delivery.domain.exception.InsufficientFundsException;
import br.com.delivery.domain.exception.InvalidAccountException;
import br.com.delivery.domain.exception.InvalidAddressException;
import br.com.delivery.domain.exception.InvalidClientException;
import br.com.delivery.domain.exception.InvalidCpfValueException;
import br.com.delivery.domain.exception.InvalidMoneyException;
import br.com.delivery.domain.exception.InvalidOrderException;
import br.com.delivery.domain.exception.InvalidOrderItemException;
import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.exception.InvalidPaymentException;
import br.com.delivery.domain.exception.InvalidRestaurantException;
import br.com.delivery.domain.exception.InvalidRestaurantOwnerException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
      OrderNotFoundException.class,
      AccountNotFoundException.class,
      RestaurantNotFoundException.class,
      MenuItemNotFoundException.class,
      ClientNotFoundException.class
  })
  public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
  }

  @ExceptionHandler({
      IllegalArgumentException.class,
      InvalidOrderException.class,
      InvalidOrderItemException.class,
      InvalidOrderItemQuantityException.class,
      CurrencyMismatchException.class,
      InvalidMoneyException.class,
      InvalidPaymentException.class,
      InsufficientFundsException.class,
      InvalidClientException.class,
      InvalidRestaurantOwnerException.class,
      InactiveAccountException.class,
      InvalidAddressException.class,
      InvalidCpfValueException.class,
      InvalidRestaurantException.class,
      InactiveItemException.class,
      InvalidAccountException.class
  })
  public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }

  private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, Exception ex, HttpServletRequest request) {
    ApiErrorResponse body = new ApiErrorResponse(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI());

    return ResponseEntity.status(status).body(body);
  }
}
