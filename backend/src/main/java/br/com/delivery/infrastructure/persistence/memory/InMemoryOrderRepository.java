package br.com.delivery.infrastructure.persistence.memory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.order.OrderStatus;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.restaurant.RestaurantId;

public final class InMemoryOrderRepository implements IOrderRepository {
  private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<Order> findById(OrderId id) {
    return Optional.ofNullable(storage.get(id.value()));
  }

  @Override
  public Optional<Order> findDraftByClientAndRestaurant(AccountId accountId, RestaurantId restaurantId) {
    return storage.values().stream()
        .filter(order -> order.getAccountId().equals(accountId))
        .filter(order -> order.getRestaurantId().equals(restaurantId))
        .filter(order -> order.getStatus() == OrderStatus.DRAFT)
        .findFirst();
  }

  @Override
  public List<Order> findAllByClientId(AccountId accountId) {
    return storage.values().stream()
        .filter(order -> order.getAccountId().equals(accountId))
        .toList();
  }

  @Override
  public void save(Order order) {
    storage.put(order.getId().value(), order);
  }
}
