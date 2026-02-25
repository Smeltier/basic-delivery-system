package br.com.delivery.domain.order;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.ZipCode;
import br.com.delivery.domain.client.ClientId;
import br.com.delivery.domain.exception.CurrencyMismatchException;
import br.com.delivery.domain.exception.InvalidOrderOperationException;
import br.com.delivery.domain.payment.PaymentId;
import br.com.delivery.domain.item.MenuItemCategory;
import br.com.delivery.domain.item.MenuItemId;

public class OrderTest {
  private final Address address = new Address("rua", "123", "casa", "cidade", "país", new ZipCode("36703-072"));

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

    MenuItemId menuItemId = MenuItemId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(menuItemId, "item", "description", MenuItemCategory.DESSERT, price, 2);

    Money total = Money.of(100.0, Currency.BRL);

    assertEquals(1, order.getItems().size());
    assertEquals(total, order.total());
  }

  @Test
  void shouldRemoveItemSuccessfuly() {
    ClientId clientId = ClientId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(clientId, brl);

    MenuItemId menuItemId = MenuItemId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(menuItemId, "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.removeItem(menuItemId);

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

    Money price = Money.of(50.0, Currency.BRL);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.CAD));

    order.markAsPaid();
    assertEquals(OrderStatus.PAID, order.getStatus());

    order.confirm();

    assertEquals(OrderStatus.CONFIRMED, order.getStatus());
  }

  @Test
  void shouldThrowWhenAddItemOutsideCreatedStatus() {
    Order order = Order.create(ClientId.generate(), Currency.CAD);
    order.cancel();

    Money price = Money.of(50.0, Currency.BRL);
    assertThrows(InvalidOrderOperationException.class,
        () -> order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2));
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
  void shouldThrowWhenChangeAddressOutsideCreatedStatus() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);
    order.cancel();

    assertThrows(InvalidOrderOperationException.class,
        () -> order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL)));
  }

  @Test
  void shouldThrowWhenMarkingAsPaidWithoutItems() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);
    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL));

    assertThrows(InvalidOrderOperationException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldThrowWhenMarkingAsPaidWithoutAddress() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);

    Money price = Money.of(50.0, Currency.BRL);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    assertThrows(InvalidOrderOperationException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldNotAllowExternalModificationOfItemsList() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);
    List<OrderItem> items = order.getItems();

    Money price = Money.of(50.0, Currency.BRL);
    assertThrows(UnsupportedOperationException.class,
        () -> items
            .add(new OrderItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2)));
  }

  @Test
  void shouldAllowCancelWhenStatusIsPaid() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);

    Money price = Money.of(50.0, Currency.BRL);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL));

    order.cancel();

    assertEquals(OrderStatus.CANCELLED, order.getStatus());
  }

  @Test
  void shouldThrowWhenAddingItemWithDifferentCurrency() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);
    Money usdPrice = Money.of(10.0, Currency.USD);

    assertThrows(CurrencyMismatchException.class,
        () -> order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, usdPrice, 2));
  }

  @Test
  void shouldThrowWhenAddingDeliveryFeeWithDifferentCurrency() {
    Order order = Order.create(ClientId.generate(), Currency.BRL);

    assertThrows(CurrencyMismatchException.class,
        () -> order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.USD)));
  }
}
