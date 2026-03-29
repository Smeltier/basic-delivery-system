package br.com.delivery.domain.repositories;

import java.util.List;
import java.util.Optional;

import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.restaurant.RestaurantId;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.order.Order;

public interface IOrderRepository {
  Optional<Order> findById(OrderId id);

  Optional<Order> findDraftByClientAndRestaurant(AccountId accountId, RestaurantId restaurantId);

  List<Order> findAllByClientId(AccountId accountId);

  void save(Order order);
}
