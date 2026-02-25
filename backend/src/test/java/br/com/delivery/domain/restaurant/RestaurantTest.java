package br.com.delivery.domain.restaurant;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.exception.InvalidRestaurantException;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Currency;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.shared.ZipCode;

public class RestaurantTest {
  private final AccountId ownerId = AccountId.generate();
  private final LocalTime open = LocalTime.of(8, 0);
  private final LocalTime close = LocalTime.of(18, 0);
  private final OpeningHours hours = new OpeningHours(open, close);
  private final ZipCode zipCode = new ZipCode("36704-072");
  private final Address address = new Address("street", "2", "House", "city", "country", zipCode);
  private final Money money = Money.of(10, Currency.BRL);

  @Test
  void shouldOpenRestaurantSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    restaurant.openRestaurant(LocalTime.of(10, 5));

    assertTrue(restaurant.isOpen());
  }

  @Test
  void shouldCloseRestaurantSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    restaurant.openRestaurant(LocalTime.of(10, 5));
    restaurant.closeRestaurant();

    assertTrue(restaurant.isClosed());
  }

  @Test
  void shouldAddProductSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);

    assertEquals(1, restaurant.getMenu().size());
  }

  @Test
  void shouldThrowWhenAddProductWithNullName() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.addMenuItem(null, "description", MenuItemCategory.DESSERT, money));
  }

  @Test
  void shouldThrowWhenAddProductWithNullUnitPrice() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, null));
  }

  @Test
  void shouldChangeNameSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    restaurant.changeName("new name");

    assertEquals("new name", restaurant.getName());
  }

  @Test
  void shouldThrowWhenChangeNameWhileOpen() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    restaurant.openRestaurant(LocalTime.of(10, 15));

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.changeName("new name"));
  }

  @Test
  void shouldStartWithStatusClosed() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertEquals(RestaurantStatus.CLOSED, restaurant.getStatus());
  }

  @Test
  void shouldThrowWhenOpenRestaurantWithBlankMenu() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.openRestaurant(LocalTime.now()));
  }

  @Test
  void shouldThrowWhenChangeNameWithBlankString() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.changeName(""));
  }

  @Test
  void shouldThrowWhenChangeNameWithNullString() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(NullPointerException.class,
        () -> restaurant.changeName(null));
  }

  @Test
  void shouldThrowWhenOpenRestaurantOutsideWorkTime() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);

    LocalTime fakeTime = LocalTime.of(5, 10);
    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.openRestaurant(fakeTime));
  }

  @Test
  void shouldThrowWhenOpenAOpenedRestaurant() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    restaurant.openRestaurant(LocalTime.now());
    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.openRestaurant(LocalTime.now()));
  }

  @Test
  void shouldBeEqualWhenSameId() {
    RestaurantId id = RestaurantId.generate();
    Restaurant r1 = Restaurant.restore(id, ownerId, "restaurant1", hours, address, RestaurantStatus.OPEN,
        new ArrayList<MenuItem>());
    Restaurant r2 = Restaurant.restore(id, ownerId, "restaurant1", hours, address, RestaurantStatus.OPEN,
        new ArrayList<MenuItem>());

    assertEquals(r1, r2);
  }

  @Test
  void shouldNotBeEqualWhenDifferentId() {
    Restaurant r1 = Restaurant.create(ownerId, "restaurant1", hours, address);
    Restaurant r2 = Restaurant.create(ownerId, "restaurant2", hours, address);

    assertNotEquals(r1, r2);
  }

  @Test
  void shouldRestoreRestaurant() {
    RestaurantId id = RestaurantId.generate();

    Restaurant restaurant = Restaurant.restore(id, ownerId, "restaurant1", hours, address, RestaurantStatus.OPEN,
        new ArrayList<MenuItem>());

    assertEquals(id, restaurant.getId());
  }

  @Test
  void shouldChangeAddressSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    Address newAddress = new Address("new street", "3", "Apt", "new city", "new country", zipCode);

    restaurant.changeAddress(newAddress);

    assertEquals(newAddress, restaurant.getAddress());
  }

  @Test
  void shouldThrowWhenChangeAddressWithNull() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);

    assertThrows(NullPointerException.class,
        () -> restaurant.changeAddress(null));
  }

  @Test
  void shouldRemoveItemSuccessfully() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    MenuItem item = restaurant.getMenu().get(0);

    restaurant.removeMenuItem(item.getId());

    assertTrue(restaurant.getMenu().isEmpty());
  }

  @Test
  void shouldThrowWhenRemoveItemWhileOpen() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    MenuItem item = restaurant.getMenu().get(0);
    restaurant.openRestaurant(LocalTime.of(10, 0));

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.removeMenuItem(item.getId()));
  }

  @Test
  void shouldNotThrowWhenRemoveNonexistentItem() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    MenuItem item = restaurant.getMenu().get(0);
    restaurant.removeMenuItem(item.getId());

    assertDoesNotThrow(() -> restaurant.removeMenuItem(item.getId()));
  }

  @Test
  void shouldThrowWhenAddItemWhileOpen() {
    Restaurant restaurant = Restaurant.create(ownerId, "restaurant", hours, address);
    restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money);
    restaurant.openRestaurant(LocalTime.of(10, 5));

    assertThrows(InvalidRestaurantException.class,
        () -> restaurant.addMenuItem("item", "description", MenuItemCategory.DESSERT, money));
  }
}