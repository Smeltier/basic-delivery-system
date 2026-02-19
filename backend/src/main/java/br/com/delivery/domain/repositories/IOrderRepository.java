package br.com.delivery.domain.repositories;

import java.util.Optional;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.order.Order;

public interface IOrderRepository {
  Optional<Order> findById(OrderId id);

  void save(Order order);
}
