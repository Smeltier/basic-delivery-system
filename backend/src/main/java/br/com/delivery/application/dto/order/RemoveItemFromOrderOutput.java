package br.com.delivery.application.dto.order;

import java.util.List;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.shared.Money;

public record RemoveItemFromOrderOutput(OrderId orderId, Money newTotal, List<OrderItemOutput> remainingItems) {
}
