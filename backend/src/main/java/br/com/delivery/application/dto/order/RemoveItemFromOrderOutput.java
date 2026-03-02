package br.com.delivery.application.dto.order;

import java.util.List;
import java.util.Objects;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.shared.Money;

public record RemoveItemFromOrderOutput(OrderId orderId, Money newTotal, List<OrderItemOutput> remainingItems) {
  public RemoveItemFromOrderOutput {
    orderId = Objects.requireNonNull(orderId, "ID do pedido não pode ser nulo.");
    newTotal = Objects.requireNonNull(newTotal, "Total do pedido não pode ser nulo.");
    remainingItems = Objects.requireNonNull(remainingItems, "Lista de itens restantes não pode ser nula.");
  }
}