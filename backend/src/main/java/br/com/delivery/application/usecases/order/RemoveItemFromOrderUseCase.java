package br.com.delivery.application.usecases.order;

import java.util.Objects;

import br.com.delivery.application.dto.order.RemoveItemFromOrderInput;
import br.com.delivery.application.dto.order.RemoveItemFromOrderOutput;
import br.com.delivery.application.exceptions.OrderNotFoundException;
import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.order.OrderId;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.restaurant.MenuItemId;

public class RemoveItemFromOrderUseCase {
  private final IOrderRepository orderRepository;

  public RemoveItemFromOrderUseCase(IOrderRepository orderRepository) {
    this.orderRepository = Objects.requireNonNull(orderRepository);
  }

  public RemoveItemFromOrderOutput execute(RemoveItemFromOrderInput input) {
    input = Objects.requireNonNull(input);

    OrderId orderId = input.orderId();
    MenuItemId menuItemId = input.menuItemId();
    int quantity = input.quantity();

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado."));

    order.decreaseItem(menuItemId, quantity);

    orderRepository.save(order);
    return new RemoveItemFromOrderOutput();
  }
}
