package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.shared.Money;

public record OrderItemOutput(MenuItemId menuItemId, int quantity, Money unitPrice) {
  public OrderItemOutput {
    menuItemId = Objects.requireNonNull(menuItemId, "O ID do item do menu não pode ser nulo.");
    unitPrice = Objects.requireNonNull(unitPrice, "O preço unitário não pode ser nulo.");

    if (quantity <= 0) {
      throw new InvalidOrderItemQuantityException("A quantidade deve ser maior que zero.");
    }
  }
}