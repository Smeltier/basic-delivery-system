package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.restaurant.MenuItemId;

public record RemoveItemFromOrderInput(OrderId orderId, MenuItemId menuItemId) {
    public RemoveItemFromOrderInput {
        orderId = Objects.requireNonNull(orderId, "ID do pedido não pode ser nulo.");
        menuItemId = Objects.requireNonNull(menuItemId, "ID do item do menu não pode ser nulo.");
    }
}
