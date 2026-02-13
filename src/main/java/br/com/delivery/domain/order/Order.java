package br.com.delivery.domain.order;

import java.util.ArrayList;
import java.util.Objects;

import br.com.delivery.domain.client.ClientId;
import br.com.delivery.domain.product.ProductId;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.payment.PaymentId;

public class Order {
  private final OrderId id;
  private final ClientId clientId;
  private final Currency currency;
  private final ArrayList<OrderItem> items;
  private final ArrayList<PaymentId> payments;
  private OrderState state;

  public Order(OrderId id, ClientId clientId, Currency currency) {
    this.id = Objects.requireNonNull(id);
    this.clientId = Objects.requireNonNull(clientId);
    this.currency = Objects.requireNonNull(currency);

    this.state = new CreatedState();
    this.items = new ArrayList<>();
    this.payments = new ArrayList<>();
  }

  public void addItem(ProductId productId, String productName, Money unitPrice, int quantity) {
    OrderItem item = new OrderItem(productId, productName, unitPrice, quantity);
    state.addItem(this, item);
  }

  public void addPayment(PaymentId paymentId) {
    payments.add(paymentId);
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

  public ClientId getClientId() {
    return clientId;
  }

  protected void internalAddItem(OrderItem item) {
    items.add(item);
  }

  protected void changeState(OrderState newState) {
    this.state = newState;
  }
}
