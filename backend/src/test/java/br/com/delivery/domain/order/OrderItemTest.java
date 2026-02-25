package br.com.delivery.domain.order;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.delivery.domain.exception.InvalidOrderItemException;
import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.item.MenuItemCategory;
import br.com.delivery.domain.item.MenuItemId;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;

public class OrderItemTest {
  private final MenuItemId menuItemId = MenuItemId.generate();
  private final String menuItemName = "item";
  private final String menuItemDescription = "description";
  private final Money unitPrice = Money.zero(Currency.BRL);

  @Test
  void shouldThrowWhenQuantityIsNegative() {
    assertThrows(InvalidOrderItemQuantityException.class,
        () -> new OrderItem(menuItemId, menuItemName, menuItemDescription, MenuItemCategory.DESSERT, unitPrice, -10));
  }

  @Test
  void shouldThrowWhenQuantityIsZero() {
    assertThrows(InvalidOrderItemQuantityException.class,
        () -> new OrderItem(menuItemId, menuItemName, menuItemDescription, MenuItemCategory.DESSERT, unitPrice, 0));
  }

  @Test
  void shouldThrowWhenProductNameIsBlank() {
    assertThrows(InvalidOrderItemException.class,
        () -> new OrderItem(menuItemId, "", menuItemDescription, MenuItemCategory.DESSERT, unitPrice, 2));
  }

  @Test
  void shouldCreateOrderItemWithCorrectValue() {
    OrderItem item = new OrderItem(menuItemId, menuItemName, menuItemDescription, MenuItemCategory.DESSERT, unitPrice,
        2);
    assertEquals(this.menuItemId, item.getMenuItemId());
    assertEquals(this.menuItemName, item.getMenuItemName());
    assertEquals(this.menuItemDescription, item.getMenuItemDescription());
    assertEquals(MenuItemCategory.DESSERT, item.getMenuItemCategory());
    assertEquals(this.unitPrice, item.getUnitPrice());
    assertEquals(2, item.getQuantity());
  }

  @Test
  void shouldCalculateTotalCorrectly() {
    Money price = new Money(new BigDecimal("10.50"), Currency.BRL);
    OrderItem item = new OrderItem(menuItemId, menuItemName, menuItemDescription, MenuItemCategory.DESSERT, price, 2);

    Money total = new Money(new BigDecimal("21.00"), Currency.BRL);

    assertEquals(total, item.total());
  }
}
