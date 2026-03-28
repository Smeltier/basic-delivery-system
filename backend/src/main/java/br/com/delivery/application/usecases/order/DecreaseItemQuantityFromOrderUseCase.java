package br.com.delivery.application.usecases.order;

import java.util.Objects;
import java.util.List;

import br.com.delivery.application.dto.order.OrderItemOutput;
import br.com.delivery.application.dto.order.DecreaseItemQuantityFromOrderInput;
import br.com.delivery.application.dto.order.DecreaseItemQuantityFromOrderOutput;
import br.com.delivery.application.exceptions.OrderNotFoundException;
import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.restaurant.MenuItemId;

public class DecreaseItemQuantityFromOrderUseCase {
  private final IOrderRepository orderRepository;

  public DecreaseItemQuantityFromOrderUseCase(IOrderRepository orderRepository) {
    this.orderRepository = Objects.requireNonNull(orderRepository);
  }

  public DecreaseItemQuantityFromOrderOutput execute(DecreaseItemQuantityFromOrderInput input) {
    input = Objects.requireNonNull(input);

    OrderId orderId = input.orderId();
    MenuItemId menuItemId = input.menuItemId();
    int quantity = input.quantity();

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado."));

    order.decreaseItem(menuItemId, quantity);
    orderRepository.save(order);

    List<OrderItemOutput> items = order.getItems().stream()
        .map(item -> new OrderItemOutput(item.getMenuItemId(), item.getQuantity(), item.getUnitPrice()))
        .toList();

    return new DecreaseItemQuantityFromOrderOutput(orderId, order.total(), items);
  }
}
