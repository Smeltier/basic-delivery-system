package br.com.delivery.application.dto.order;

import java.util.List;
import java.util.Objects;

import br.com.delivery.domain.order.Order;

public record FindClientOrdersOutput(List<Order> orders) {
  public FindClientOrdersOutput {
    orders = Objects.requireNonNull(orders);
  }
}
