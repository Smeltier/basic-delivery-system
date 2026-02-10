package br.com.delivery.domain.order;

public interface OrderState {
  void addItem(Order order, OrderItem item);

  void pay(Order order);

  void cancel(Order order);
}
