package br.com.delivery.domain.restaurant;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.delivery.domain.exception.InactiveItemException;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;

public class MenuItemTest {
  @Test
  void shouldThrowWhenChangePriceInAInactiveProduct() {
    Money price = Money.of(5, Currency.BRL);
    MenuItemId id = MenuItemId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    MenuItemCategory category = MenuItemCategory.DESSERT;
    MenuItem item = new MenuItem(id, restaurantId, "item", "description", category, price);
    item.deactivate();

    assertThrows(InactiveItemException.class,
        () -> item.changePrice(Money.of(2, Currency.BRL)));
  }

  @Test
  void shouldCreateValidMenuItem() {
    Money price = Money.of(5, Currency.BRL);
    MenuItemId id = MenuItemId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    MenuItemCategory category = MenuItemCategory.DESSERT;
    MenuItem item = new MenuItem(id, restaurantId, "item", "description", category, price);

    assertEquals(price, item.currentPrice());
    assertEquals(id, item.getId());
    assertEquals(restaurantId, item.getRestaurantId());
    assertEquals(category, item.getCategory());
  }
}
