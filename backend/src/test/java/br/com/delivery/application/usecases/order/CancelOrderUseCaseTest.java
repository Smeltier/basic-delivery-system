package br.com.delivery.application.usecases.order;

import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import br.com.delivery.application.dto.order.CancelOrderInput;
import br.com.delivery.application.dto.order.CancelOrderOutput;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.order.OrderStatus;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.restaurant.RestaurantId;
import br.com.delivery.domain.shared.Currency;


public class CancelOrderUseCaseTest {
  private IOrderRepository orderRepository;
  private CancelOrderUseCase cancelOrderUseCase;

  @BeforeEach
  void setup() {
    orderRepository = new FakeOrderRepository();
    cancelOrderUseCase = new CancelOrderUseCase(orderRepository);
  }

  @Test
  void shouldCancelExistingOrder() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    orderRepository.save(order);

    CancelOrderInput input = new CancelOrderInput(order.getId());
    CancelOrderOutput output = cancelOrderUseCase.execute(input);

    Order savedOrder = orderRepository.findById(order.getId()).orElse(null);
    assertNotNull(savedOrder);

    assertEquals(OrderStatus.CANCELLED, savedOrder.getStatus());
    assertEquals(order.getId(), output.orderId());
  }

  @Test
  void shouldThrowWhenOrderNotFound() {
    CancelOrderInput input = new CancelOrderInput(OrderId.generate());
    assertThrows(br.com.delivery.application.exceptions.OrderNotFoundException.class, () -> {
      cancelOrderUseCase.execute(input);
    });
  }

  @Test
  void shouldNotCancelAlreadyCancelledOrder() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.markAsCancelled();
    orderRepository.save(order);

    CancelOrderInput input = new CancelOrderInput(order.getId());
    CancelOrderOutput output = cancelOrderUseCase.execute(input);

    assertTrue(output.success());
  }

  private static class FakeOrderRepository implements br.com.delivery.domain.repositories.IOrderRepository {
    private final Map<OrderId, Order> storage = new HashMap<>();

    @Override
    public Optional<Order> findById(OrderId id) {
      return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Order> findDraftByClientAndRestaurant(AccountId accountId, RestaurantId restaurantId) {
      return storage.values().stream()
          .filter(o -> o.getAccountId().equals(accountId) && o.getRestaurantId().equals(restaurantId)
              && o.getStatus() == br.com.delivery.domain.order.OrderStatus.DRAFT)
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
      storage.put(order.getId(), order);
    }
  }
}
