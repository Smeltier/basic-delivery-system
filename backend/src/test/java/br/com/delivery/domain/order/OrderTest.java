package br.com.delivery.domain.order;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.ZipCode;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.exception.CurrencyMismatchException;
import br.com.delivery.domain.exception.InvalidOrderException;
import br.com.delivery.domain.payment.PaymentId;
import br.com.delivery.domain.restaurant.MenuItemCategory;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.restaurant.RestaurantId;

public class OrderTest {
  private final Address address = new Address("rua", "123", "casa", "cidade", "país", new ZipCode("36703-072"));

  @Test
  void shouldCreateOrderWithCorrectValue() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    assertEquals(clientId, order.getAccountId());
    assertEquals(brl, order.getCurrency());
    assertEquals(restaurantId, order.getRestaurantId());
  }

  @Test
  void shouldCreateWithDraftStatus() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    assertEquals(OrderStatus.DRAFT, order.getStatus());
  }

  @Test
  void paymentListShouldStartEmpty() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    assertTrue(order.getPayments().isEmpty());
    assertEquals(0, order.getPayments().size());
  }

  @Test
  void orderItemListShouldStartEmpty() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    assertTrue(order.getItems().isEmpty());
    assertEquals(0, order.getItems().size());
  }

  @Test
  void totalShouldBeZeroWhenCreated() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    assertTrue(order.total().equals(Money.zero(brl)));
  }

  @Test
  void shouldAddItemSuccessfuly() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    MenuItemId menuItemId = MenuItemId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(menuItemId, "item", "description", MenuItemCategory.DESSERT, price, 2);

    Money total = Money.of(100.0, Currency.BRL);

    assertEquals(1, order.getItems().size());
    assertEquals(total, order.total());
  }

  @Test
  void shouldRemoveItemSuccessfuly() {
    AccountId clientId = AccountId.generate();
    RestaurantId restaurantId = RestaurantId.generate();
    Currency brl = Currency.BRL;
    Order order = Order.create(restaurantId, clientId, brl);

    MenuItemId menuItemId = MenuItemId.generate();
    Money price = Money.of(50.0, Currency.BRL);

    order.addItem(menuItemId, "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.removeItem(menuItemId);

    assertEquals(0, order.getItems().size());
    assertTrue(order.getItems().isEmpty());
  }

  @Test
  void shouldRegisterPayment() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    PaymentId paymentId = PaymentId.generate();

    order.registerPayment(paymentId);

    assertEquals(1, order.getPayments().size());
    assertFalse(order.getPayments().isEmpty());
  }

  @Test
  void shouldTransitionThroughHappyPath() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.CAD);

    Money price = Money.of(50.0, Currency.CAD);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.CAD));

    order.markAsPaid();
    assertEquals(OrderStatus.PAID, order.getStatus());

    order.confirm();

    assertEquals(OrderStatus.CONFIRMED, order.getStatus());
  }

  @Test
  void shouldThrowWhenAddItemOutsideDraftStatus() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.CAD);
    order.cancel();

    Money price = Money.of(50.0, Currency.BRL);
    assertThrows(InvalidOrderException.class,
        () -> order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2));
  }

  @Test
  void shouldThrowWhenConfirmOutsidePaidStatus() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.CAD);

    assertThrows(InvalidOrderException.class,
        () -> order.confirm());
  }

  @Test
  void shouldThrowWhenPaidOutsideDraftStatus() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.CAD);
    order.cancel();

    assertThrows(InvalidOrderException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldThrowWhenChangeAddressOutsideDraftStatus() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.cancel();

    assertThrows(InvalidOrderException.class,
        () -> order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL)));
  }

  @Test
  void shouldThrowWhenMarkingAsPaidWithoutItems() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL));

    assertThrows(InvalidOrderException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldThrowWhenMarkingAsPaidWithoutAddress() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);

    Money price = Money.of(50.0, Currency.BRL);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    assertThrows(InvalidOrderException.class,
        () -> order.markAsPaid());
  }

  @Test
  void shouldNotAllowExternalModificationOfItemsList() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    List<OrderItem> items = order.getItems();

    Money price = Money.of(50.0, Currency.BRL);
    assertThrows(UnsupportedOperationException.class,
        () -> items
            .add(new OrderItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2)));
  }

  @Test
  void shouldAllowCancelWhenStatusIsPaid() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);

    Money price = Money.of(50.0, Currency.BRL);
    order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, price, 2);

    order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.BRL));

    order.cancel();

    assertEquals(OrderStatus.CANCELLED, order.getStatus());
  }

  @Test
  void shouldThrowWhenAddingItemWithDifferentCurrency() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);
    Money usdPrice = Money.of(10.0, Currency.USD);

    assertThrows(CurrencyMismatchException.class,
        () -> order.addItem(MenuItemId.generate(), "product", "description", MenuItemCategory.DESSERT, usdPrice, 2));
  }

  @Test
  void shouldThrowWhenAddingDeliveryFeeWithDifferentCurrency() {
    Order order = Order.create(RestaurantId.generate(), AccountId.generate(), Currency.BRL);

    assertThrows(CurrencyMismatchException.class,
        () -> order.changeDeliveryAddress(this.address, Money.of(5.0, Currency.USD)));
  }
}
