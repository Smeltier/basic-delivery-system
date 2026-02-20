package br.com.delivery.domain.order;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.client.ClientId;
import br.com.delivery.domain.exception.InvalidOrderOperationException;
import br.com.delivery.domain.payment.PaymentId;
import br.com.delivery.domain.product.ProductId;

public class OrderTest {
  @Test
  void shouldCreateOrderWithCorrectValue() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    assertEquals(clientId, order.getClientId());
    assertEquals(brl, order.getCurrency());
  }

  @Test
  void shouldCreateWithCreatedStatus() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    assertEquals(OrderStatus.CREATED, order.getStatus());
  }

  @Test
  void paymentListShouldStartEmpty() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    assertTrue(order.getPayments().isEmpty());
    assertEquals(0, order.getPayments().size());
  }

  @Test
  void orderItemListShouldStartEmpty() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    assertTrue(order.getItems().isEmpty());
    assertEquals(0, order.getItems().size());
  }

  @Test
  void totalShouldBeZeroWhenCreated() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    assertTrue(order.total().equals(Money.zero(brl)));
  }

  @Test
  void shouldAddItemSuccessfuly() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    ProductId productId = ProductId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(productId, "produto 1", price, 2);

    Money total = Money.of(100.0, Currency.BRL);

    assertEquals(1, order.getItems().size());
    assertEquals(total, order.total());
  }

  @Test
  void shouldRemoveItemSuccessfuly() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    ProductId productId = ProductId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(productId, "produto 1", price, 2);

    order.removeItem(productId);

    assertEquals(0, order.getItems().size());
    assertTrue(order.getItems().isEmpty());
  }

  @Test
  void shouldRegisterPayment() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);
    PaymentId paymentId = PaymentId.generate();

    order.registerPayment(paymentId);

    assertEquals(1, order.getPayments().size());
    assertFalse(order.getPayments().isEmpty());
  }

  @Test
  void shouldTransitionThroughHappyPath() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);

    order.markAsPaid();
    assertEquals(OrderStatus.PAID, order.getStatus());

    order.confirm();

    assertEquals(OrderStatus.CONFIRMED, order.getStatus());
  }

  @Test
  void shouldThrowWhenAddItemOutsideCreatedStatus() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);
    order.markAsPaid();

    assertThrows(InvalidOrderOperationException.class,
        () -> order.addItem(ProductId.generate(), "Product", Money.of(10.0, Currency.BRL), 1));
  }

  @Test
  void shouldThrowWhenConfirmOutsidePaidStatus() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);

    assertThrows(InvalidOrderOperationException.class,
        () -> order.confirm());
  }

  @Test
  void shouldThrowWhenPaidOutsideCreatedStatus() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);
    order.cancel();

    assertThrows(InvalidOrderOperationException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldThrowWhenCancelInConfirmedStatus() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);
    order.markAsPaid();
    order.confirm();

    assertThrows(InvalidOrderOperationException.class,
        () -> order.cancel());
  }
}
