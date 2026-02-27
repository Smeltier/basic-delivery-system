package br.com.delivery.application.dto.order;

import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.shared.Money;

public record OrderItemOutput(MenuItemId menuItemId, int quantity, Money unitPrice) {
}
