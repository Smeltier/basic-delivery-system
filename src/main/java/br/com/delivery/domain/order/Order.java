package br.com.delivery.domain.order;

import java.util.ArrayList;

import br.com.delivery.domain.Client;
import br.com.delivery.domain.Product;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.Currency;

public class Order {
  private final OrderId id;
  private final Client client;
  private final Currency currency;
  private final ArrayList<OrderItem> items;
  private OrderState state;

  public Order(OrderId id, Client client, Currency currency) {
    if (id == null) {
      throw new IllegalArgumentException("ID não pode ser nulo.");
    }
    if (client == null) {
      throw new IllegalArgumentException("Cliente não pode ser nulo.");
    }
    this.id = id;
    this.client = client;
    this.currency = currency;
    this.state = new CreatedState();
    this.items = new ArrayList<>();
  }

  public void addItem(Product product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Produto não pode ser nulo.");
    }
    Money unitPrice = product.currentPrice();
    OrderItem item = new OrderItem(product, quantity, unitPrice);
    state.addItem(this, item);
  }

  public void pay() {
    state.pay(this);
  }

  public void cancel() {
    state.cancel(this);
  }

  public Money total() {
    Money total = Money.zero(this.currency);
    for (OrderItem item : this.items) {
      total = total.add(item.total());
    }
    return total;
  }

  public OrderId getId() {
    return id;
  }

  public Client getClient() {
    return client;
  }

  protected void internalAddItem(OrderItem item) {
    items.add(item);
  }

  protected void changeState(OrderState newState) {
    this.state = newState;
  }
}
