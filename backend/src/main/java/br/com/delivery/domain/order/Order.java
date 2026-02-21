package br.com.delivery.domain.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Collections;

import br.com.delivery.domain.exception.CurrencyMismatchException;
import br.com.delivery.domain.exception.InvalidOrderOperationException;
import br.com.delivery.domain.client.ClientId;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.payment.PaymentId;
import br.com.delivery.domain.product.ProductId;

public class Order {
  private final OrderId id;
  private final ClientId clientId;
  private final Currency currency;
  private final List<OrderItem> items;
  private final LocalDateTime createdAt;
  private final List<PaymentId> payments;
  private OrderStatus status;
  private Address deliveryAddress;
  private Money deliveryFee;
  private LocalDateTime paidAt;
  private LocalDateTime confirmedAt;
  private LocalDateTime cancelledAt;
  private LocalDateTime deliveredAt;

  private Order(OrderId id, ClientId clientId, Currency currency, LocalDateTime createdAt) {
    this.id = Objects.requireNonNull(id);
    this.clientId = Objects.requireNonNull(clientId);
    this.currency = Objects.requireNonNull(currency);
    this.createdAt = Objects.requireNonNull(createdAt);
    this.items = new ArrayList<>();
    this.payments = new ArrayList<>();
    this.status = OrderStatus.CREATED;
    this.deliveryFee = Money.zero(currency);
  }

  public static Order create(ClientId clientId, Currency currency) {
    return new Order(OrderId.generate(), clientId, currency, LocalDateTime.now());
  }

  public static Order restore(OrderId id, ClientId clientId, Currency currency, LocalDateTime createdAt,
      OrderStatus status, List<OrderItem> items, List<PaymentId> payments, Address address, Money deliveryFee,
      LocalDateTime paidAt, LocalDateTime confirmedAt, LocalDateTime cancelledAt, LocalDateTime deliveredAt) {
    Order order = new Order(id, clientId, currency, createdAt);
    order.status = status;
    order.items.addAll(items);
    order.payments.addAll(payments);
    order.deliveryAddress = address;
    order.deliveryFee = deliveryFee;
    order.paidAt = paidAt;
    order.confirmedAt = confirmedAt;
    order.cancelledAt = cancelledAt;
    order.deliveredAt = deliveredAt;
    return order;
  }

  public void addItem(ProductId productId, String productName, Money unitPrice, int quantity) {
    if (status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException("Não pode adicionar itens no status " + status);
    }
    if (unitPrice.currency() != currency) {
      throw new CurrencyMismatchException("Não pode adicionar produtos com moedas diferentes.");
    }
    OrderItem item = new OrderItem(productId, productName, unitPrice, quantity);
    items.add(item);
  }

  public void removeItem(ProductId productId) {
    if (status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException("Não pode remover itens no status " + status);
    }
    items.removeIf(item -> item.getProductId().equals(productId));
  }

  public void changeDeliveryAddress(Address newAddress, Money newFee) {
    if (status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException("Não pode mudar o endereço de entrega no status " + status);
    }
    if (newFee.currency() != currency) {
      throw new CurrencyMismatchException("A moeda da taxa de entrega deve ser a mesma do pedido.");
    }
    this.deliveryAddress = Objects.requireNonNull(newAddress);
    this.deliveryFee = Objects.requireNonNull(newFee);
  }

  public void registerPayment(PaymentId paymentId) {
    if (status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException("Não é possível registrar pagamento no status " + status);
    }
    Objects.requireNonNull(paymentId);
    payments.add(paymentId);
  }

  public Money total() {
    Money total = Money.zero(currency);
    for (OrderItem item : this.items) {
      total = total.add(item.total());
    }
    return total;
  }

  public void markAsPaid() {
    if (status != OrderStatus.CREATED) {
      throw new InvalidOrderOperationException("O pedido não pode ser pago no estado " + status);
    }
    if (items.isEmpty()) {
      throw new InvalidOrderOperationException("Pedido deve ter pelo menos um item.");
    }
    if (deliveryAddress == null) {
      throw new InvalidOrderOperationException("Pedido deve ter um endereço de entrega.");
    }
    if (total().isZero()) {
      throw new InvalidOrderOperationException("Pedido Não pode ter total zero.");
    }
    status = OrderStatus.PAID;
    paidAt = LocalDateTime.now();
  }

  public void markAsDelivered() {
    if (status != OrderStatus.CONFIRMED) {
      throw new InvalidOrderOperationException("Pedido não pode ser entregue no estado " + status);
    }
    status = OrderStatus.DELIVERED;
    deliveredAt = LocalDateTime.now();
  }

  public void confirm() {
    if (status != OrderStatus.PAID) {
      throw new InvalidOrderOperationException("Apenas pedidos pagos podem ser confirmados.");
    }
    status = OrderStatus.CONFIRMED;
    confirmedAt = LocalDateTime.now();
  }

  public void cancel() {
    if (status == OrderStatus.DELIVERED) {
      throw new InvalidOrderOperationException("Pedidos entregues não podem ser cancelados.");
    }
    status = OrderStatus.CANCELLED;
    cancelledAt = LocalDateTime.now();
  }

  public OrderId getId() {
    return id;
  }

  public ClientId getClientId() {
    return clientId;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Money getDeliveryFee() {
    return deliveryFee;
  }

  public Address getDeliveryAddress() {
    return deliveryAddress;
  }

  public List<PaymentId> getPayments() {
    return Collections.unmodifiableList(payments);
  }

  public List<OrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public Optional<LocalDateTime> getCancelledAt() {
    return Optional.ofNullable(cancelledAt);
  }

  public Optional<LocalDateTime> getConfirmedAt() {
    return Optional.ofNullable(confirmedAt);
  }

  public Optional<LocalDateTime> getDeliveredAt() {
    return Optional.ofNullable(deliveredAt);
  }

  public Optional<LocalDateTime> getPaidAt() {
    return Optional.ofNullable(paidAt);
  }
}
