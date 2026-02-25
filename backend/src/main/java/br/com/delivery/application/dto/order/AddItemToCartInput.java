package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.restaurant.RestaurantId;

public record AddItemToCartInput(AccountId accountId, RestaurantId restaurantId, MenuItemId menuItemId, int quantity) {
  public AddItemToCartInput {
    accountId = Objects.requireNonNull(accountId);
    restaurantId = Objects.requireNonNull(restaurantId);
    menuItemId = Objects.requireNonNull(menuItemId);

    if (quantity <= 0) {
      throw new InvalidOrderItemQuantityException("A quantidade deve ser positiva.");
    }
  }
}