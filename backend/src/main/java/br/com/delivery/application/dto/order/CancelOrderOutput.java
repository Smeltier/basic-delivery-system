package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;

public record CancelOrderOutput(OrderId orderId, boolean success) {
  public CancelOrderOutput {
    orderId = Objects.requireNonNull(orderId, "O ID do pedido não pode ser nulo.");
  }
}