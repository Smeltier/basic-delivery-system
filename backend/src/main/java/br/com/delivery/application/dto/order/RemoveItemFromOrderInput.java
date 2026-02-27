package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.restaurant.MenuItemId;

public record RemoveItemFromOrderInput(OrderId orderId, MenuItemId menuItemId, int quantity) { 
  public RemoveItemFromOrderInput {
    orderId = Objects.requireNonNull(orderId);
    menuItemId = Objects.requireNonNull(menuItemId);

    if (quantity <= 0) {
      throw new InvalidOrderItemQuantityException("Quantidade inválida.");
    }
  }
}