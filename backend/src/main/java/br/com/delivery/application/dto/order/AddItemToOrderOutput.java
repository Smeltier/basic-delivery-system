package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;

public record AddItemToOrderOutput(OrderId orderId) {
  public AddItemToOrderOutput {
    orderId = Objects.requireNonNull(orderId);
  }
}