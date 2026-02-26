package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.order.OrderId;

public record AddItemToCartOutput(OrderId orderId) {
  public AddItemToCartOutput {
    orderId = Objects.requireNonNull(orderId);
  }
}