package br.com.delivery.domain.restaurant;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.exception.InvalidRestaurantOperationException;
import br.com.delivery.domain.product.ProductId;
import br.com.delivery.domain.product.Product;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.ZipCode;

public class RestaurantTest {
  private final LocalTime open = LocalTime.of(8, 0);
  private final LocalTime close = LocalTime.of(18, 0);
  private final OpeningHours hours = new OpeningHours(open, close);
  private final ZipCode zipCode = new ZipCode("36704-072");
  private final Address address = new Address("street", "2", "House", "city", "country", zipCode);

  @Test
  void shouldOpenRestaurantSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    restaurant.openRestaurant(LocalTime.of(10, 5));

    assertTrue(restaurant.isOpen());
  }

  @Test
  void shouldCloseRestaurantSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    restaurant.openRestaurant(LocalTime.of(10, 5));
    restaurant.closeRestaurant();

    assertTrue(restaurant.isClosed());
  }

  @Test
  void shouldAddProductSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));

    assertEquals(1, restaurant.getMenu().size());
  }

  @Test
  void shouldThrowWhenAddProductWithNullName() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.addItem(null, Money.of(10, Currency.BRL)));
  }

  @Test
  void shouldThrowWhenAddProductWithNullUnitPrice() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.addItem("product", null));
  }

  @Test
  void shouldChangeNameSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    restaurant.changeName("new name");

    assertEquals("new name", restaurant.getName());
  }

  @Test
  void shouldThrowWhenChangeNameWhileOpen() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    restaurant.openRestaurant(LocalTime.of(10, 15));

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.changeName("new name"));
  }

  @Test
  void shouldStartWithStatusClosed() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertEquals(RestaurantStatus.CLOSED, restaurant.getStatus());
  }

  @Test
  void shouldThrowWhenOpenRestaurantWithBlankMenu() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.openRestaurant(LocalTime.now()));
  }

  @Test
  void shouldThrowWhenChangeNameWithBlankString() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.changeName(""));
  }

  @Test
  void shouldThrowWhenChangeNameWithNullString() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(NullPointerException.class,
        () -> restaurant.changeName(null));
  }

  @Test
  void shouldThrowWhenOpenRestaurantOutsideWorkTime() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));

    LocalTime fakeTime = LocalTime.of(5, 10);
    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.openRestaurant(fakeTime));
  }

  @Test
  void shouldThrowWhenOpenAOpenedRestaurant() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    restaurant.openRestaurant(LocalTime.now());
    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.openRestaurant(LocalTime.now()));
  }

  @Test
  void shouldBeEqualWhenSameId() {
    RestaurantId id = RestaurantId.generate();
    Restaurant r1 = Restaurant.restore(id, "restaurant1", hours, address);
    Restaurant r2 = Restaurant.restore(id, "restaurant2", hours, address);

    assertEquals(r1, r2);
  }

  @Test
  void shouldNotBeEqualWhenDifferentId() {
    Restaurant r1 = Restaurant.create("restaurant1", hours, address);
    Restaurant r2 = Restaurant.create("restaurant2", hours, address);

    assertNotEquals(r1, r2);
  }

  @Test
  void shouldRestoreRestaurant() {
    RestaurantId id = RestaurantId.generate();

    Restaurant restaurant = Restaurant.restore(id, "restaurant", hours, address);

    assertEquals(id, restaurant.getId());
  }

  @Test
  void shouldChangeAddressSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    Address newAddress = new Address("new street", "3", "Apt", "new city", "new country", zipCode);

    restaurant.changeAddress(newAddress);

    assertEquals(newAddress, restaurant.getAddress());
  }

  @Test
  void shouldThrowWhenChangeAddressWithNull() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);

    assertThrows(NullPointerException.class,
        () -> restaurant.changeAddress(null));
  }

  @Test
  void shouldRemoveItemSuccessfully() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    Product product = restaurant.getMenu().get(0);

    restaurant.removeItem(product.getId());

    assertTrue(restaurant.getMenu().isEmpty());
  }

  @Test
  void shouldThrowWhenRemoveItemWhileOpen() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    Product product = restaurant.getMenu().get(0);
    restaurant.openRestaurant(LocalTime.of(10, 0));

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.removeItem(product.getId()));
  }

  @Test
  void shouldNotThrowWhenRemoveNonexistentItem() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product", Money.of(5, Currency.BRL));
    Product product = restaurant.getMenu().get(0);
    restaurant.removeItem(product.getId());

    assertDoesNotThrow(() -> restaurant.removeItem(product.getId()));
  }

  @Test
  void shouldThrowWhenAddItemWhileOpen() {
    Restaurant restaurant = Restaurant.create("restaurant", hours, address);
    restaurant.addItem("product1", Money.of(5, Currency.BRL));
    restaurant.openRestaurant(LocalTime.of(10, 5));

    assertThrows(InvalidRestaurantOperationException.class,
        () -> restaurant.addItem("product2", Money.of(10, Currency.BRL)));
  }
}
