package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;

public record CreateOrderOutput(OrderId orderId) {
  public CreateOrderOutput {
    orderId = Objects.requireNonNull(orderId);
  }
}