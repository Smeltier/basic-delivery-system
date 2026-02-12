package br.com.delivery.domain.order;

import java.util.Optional;

public interface OrderRepository {
  Optional<Order> findById(OrderId id);

  void save(Order order);
}
