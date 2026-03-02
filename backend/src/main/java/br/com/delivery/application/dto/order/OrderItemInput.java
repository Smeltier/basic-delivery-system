package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;

public record OrderItemInput(OrderId orderId, boolean cancelledStatus) {
  public OrderItemInput {
    orderId = Objects.requireNonNull(orderId, "ID do Pedido não pode ser nulo.");
  }
}