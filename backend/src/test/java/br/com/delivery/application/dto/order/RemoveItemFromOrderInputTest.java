package br.com.delivery.application.dto.order;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;

public class RemoveItemFromOrderInputTest {
  @Test
  void shouldThrowWhenInputOrderIdIsNull() {
    assertThrows(NullPointerException.class,
        () -> new RemoveItemFromOrderInput(null, MenuItemId.generate(), 2));
  }

  @Test
  void shouldThrowWhenInputMenuItemIdIsNull() {
    assertThrows(NullPointerException.class,
        () -> new RemoveItemFromOrderInput(OrderId.generate(), null, 2));
  }

  @Test
  void shouldThrowWhenInputQuantityIsNegative() {
    assertThrows(InvalidOrderItemQuantityException.class,
        () -> new RemoveItemFromOrderInput(OrderId.generate(), MenuItemId.generate(), -10));
  }

  @Test
  void shouldThrowWhenInputQuantityIsZero() {
    assertThrows(InvalidOrderItemQuantityException.class,
        () -> new RemoveItemFromOrderInput(OrderId.generate(), MenuItemId.generate(), 0));
  }
}
