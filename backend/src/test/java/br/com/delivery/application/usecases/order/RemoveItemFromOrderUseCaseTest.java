package br.com.delivery.application.usecases.order;

import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.application.dto.order.RemoveItemFromOrderOutput;
import br.com.delivery.application.exceptions.OrderNotFoundException;
import br.com.delivery.application.dto.order.RemoveItemFromOrderInput;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.exception.InvalidOrderException;
import br.com.delivery.domain.exception.InvalidOrderItemException;
import br.com.delivery.domain.restaurant.MenuItemCategory;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.restaurant.RestaurantId;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.order.OrderItem;

public class RemoveItemFromOrderUseCaseTest {
  private FakeOrderRepository orderRepo;
  private RemoveItemFromOrderUseCase useCase;

  @BeforeEach
  void setup() {
    this.orderRepo = new FakeOrderRepository();
    this.useCase = new RemoveItemFromOrderUseCase(orderRepo);
  }

  @Test
  void shouldThrowWhenIOrderRepositoryIsNull() {
    assertThrows(NullPointerException.class,
        () -> new RemoveItemFromOrderUseCase(null));
  }

  @Test
  void shouldThrowWhenInputIsNull() {
    assertThrows(NullPointerException.class,
        () -> useCase.execute(null));
  }

  @Test
  void shouldRemoveItemSuccessfullyWhenQuantityEqualsToItemQuantity() {
    MenuItemId menuItemId = MenuItemId.generate();

    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.addItem(menuItemId, "name", "description", MenuItemCategory.DESSERT, Money.of(10, Currency.BRL), 5);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 5);
    RemoveItemFromOrderOutput output = useCase.execute(input);

    assertNotNull(output);

    Order savedOrder = orderRepo.findById(order.getId()).orElseThrow();
    assertTrue(savedOrder.getItems().isEmpty());
  }

  @Test
  void shouldRemoveItemQuantitySuccessfully() {
    MenuItemId menuItemId = MenuItemId.generate();

    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.addItem(menuItemId, "name", "description", MenuItemCategory.DESSERT, Money.of(10, Currency.BRL), 5);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 3);

    RemoveItemFromOrderOutput output = useCase.execute(input);
    assertNotNull(output);

    Order savedOrder = orderRepo.findById(order.getId()).orElseThrow();
    assertNotNull(savedOrder);

    OrderItem item = savedOrder.getItems().get(0);
    assertNotNull(item);

    assertEquals(2, item.getQuantity());
  }

  @Test
  void shouldThrowWhenQuantityIsInvalid() {
    MenuItemId menuItemId = MenuItemId.generate();

    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.addItem(menuItemId, "name", "description", MenuItemCategory.DESSERT, Money.of(10, Currency.BRL), 5);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 10);

    assertThrows(InvalidOrderItemException.class,
        () -> useCase.execute(input));
  }

  @Test
  void shouldThrowWhenOrderNotFound() {
    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(OrderId.generate(), MenuItemId.generate(), 5);
    assertThrows(OrderNotFoundException.class,
        () -> useCase.execute(input));
  }

  @Test
  void shouldThrowWhenItemDoesNotExistte() {
    MenuItemId menuItemId = MenuItemId.generate();

    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 10);

    assertThrows(InvalidOrderException.class,
        () -> useCase.execute(input));
  }

  @Test
  void shouldReturnCorrectOutputDataWhenRemovingPartialQuantity() {
    MenuItemId menuItemId = MenuItemId.generate();
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.addItem(menuItemId, "name", "description", MenuItemCategory.DESSERT, Money.of(10, Currency.BRL), 5);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 3);
    RemoveItemFromOrderOutput output = useCase.execute(input);

    assertEquals(order.getId(), output.orderId());
    assertEquals(Money.of(20, Currency.BRL), output.newTotal());
    assertEquals(1, output.remainingItems().size());
    assertEquals(menuItemId, output.remainingItems().get(0).menuItemId());
    assertEquals(2, output.remainingItems().get(0).quantity());
    assertEquals(Money.of(10, Currency.BRL), output.remainingItems().get(0).unitPrice());
  }

  @Test
  void shouldReturnCorrectOutputDataWhenRemovingTotalQuantity() {
    MenuItemId menuItemId = MenuItemId.generate();
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.addItem(menuItemId, "name", "description", MenuItemCategory.DESSERT, Money.of(10, Currency.BRL), 5);
    this.orderRepo.save(order);

    RemoveItemFromOrderInput input = new RemoveItemFromOrderInput(order.getId(), menuItemId, 5);
    RemoveItemFromOrderOutput output = useCase.execute(input);

    assertEquals(order.getId(), output.orderId());
    assertEquals(Money.of(0.0, Currency.BRL), output.newTotal());
    assertTrue(output.remainingItems().isEmpty());
  }

  private static class FakeOrderRepository implements br.com.delivery.domain.repositories.IOrderRepository {
    private final Map<OrderId, Order> map = new HashMap<>();

    @Override
    public Optional<Order> findById(OrderId id) {
      return Optional.ofNullable(map.get(id));
    }

    @Override
    public Optional<Order> findDraftByClientAndRestaurant(AccountId accountId, RestaurantId restaurantId) {
      return map.values().stream()
          .filter(o -> o.getAccountId().equals(accountId) && o.getRestaurantId().equals(restaurantId)
              && o.getStatus() == br.com.delivery.domain.order.OrderStatus.DRAFT)
          .findFirst();
    }

    @Override
    public void save(Order order) {
      map.put(order.getId(), order);
    }
  }
}
