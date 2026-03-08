package br.com.delivery.application.dto.order;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.Currency;

public class OrderItemOutputTest {
  @Test
  public void testValidOrderItemOutput() {
    MenuItemId menuItemId = MenuItemId.generate();
    int quantity = 2;
    Money unitPrice = Money.of(5.0, Currency.BRL);

    OrderItemOutput orderItemOutput = new OrderItemOutput(menuItemId, quantity, unitPrice);

    assertEquals(menuItemId, orderItemOutput.menuItemId());
    assertEquals(quantity, orderItemOutput.quantity());
    assertEquals(unitPrice, orderItemOutput.unitPrice());
  }

  @Test
  public void testInvalidQuantity() {
    MenuItemId menuItemId = MenuItemId.generate();
    Money unitPrice = Money.of(5.0, Currency.BRL);
    int invalidQuantity = 0;

    assertThrows(InvalidOrderItemQuantityException.class, 
        () -> {new OrderItemOutput(menuItemId, invalidQuantity, unitPrice);});
  }

  @Test
  public void testNegativeQuantity() {
    MenuItemId menuItemId = MenuItemId.generate();
    Money unitPrice = Money.of(5.0, Currency.BRL);
    int negativeQuantity = -1;

    assertThrows(InvalidOrderItemQuantityException.class, 
        () -> {new OrderItemOutput(menuItemId, negativeQuantity, unitPrice);});
  }
}