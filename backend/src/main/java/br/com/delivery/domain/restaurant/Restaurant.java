package br.com.delivery.domain.restaurant;

import java.time.LocalTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.delivery.domain.exception.InvalidRestaurantOperationException;
import br.com.delivery.domain.item.MenuItem;
import br.com.delivery.domain.item.MenuItemCategory;
import br.com.delivery.domain.item.MenuItemId;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Money;

// TODO: add owner refference.

public final class Restaurant {
  private final RestaurantId id;
  private final List<MenuItem> menu;
  private String name;
  private RestaurantStatus status;
  private OpeningHours openingHours;
  private Address address;

  private Restaurant(RestaurantId id, String name, OpeningHours openingHours, Address address) {
    this.id = Objects.requireNonNull(id);
    this.openingHours = Objects.requireNonNull(openingHours);
    this.status = RestaurantStatus.CLOSED;
    this.menu = new ArrayList<>();

    changeAddress(address);
    changeName(name);
  }

  public static Restaurant create(String name, OpeningHours openingHours, Address address) {
    return new Restaurant(RestaurantId.generate(), name, openingHours, address);
  }

  public static Restaurant restore(RestaurantId id, String name, OpeningHours openingHours, Address address,
      RestaurantStatus status, List<MenuItem> menu) {
    Restaurant restaurant = new Restaurant(id, name, openingHours, address);
    restaurant.status = status;
    restaurant.menu.addAll(menu);
    return restaurant;
  }

  public void addMenuItem(String itemName, String itemDescription, MenuItemCategory category, Money unitPrice) {
    if (status != RestaurantStatus.CLOSED) {
      throw new InvalidRestaurantOperationException("Não pode adicionar itens com o restaurante ainda aberto.");
    }

    if (itemName == null) {
      throw new InvalidRestaurantOperationException("Nome do produto não deve ser nulo.");
    }

    if (unitPrice == null) {
      throw new InvalidRestaurantOperationException("Preço unitário do produto não deve ser nulo.");
    }

    MenuItem item = new MenuItem(MenuItemId.generate(), id, itemName, itemDescription, category, unitPrice);
    this.menu.add(item);
  }

  public void removeMenuItem(MenuItemId productId) {
    if (status != RestaurantStatus.CLOSED) {
      throw new InvalidRestaurantOperationException("Não pode remover itens com o restaurante ainda aberto.");
    }

    menu.removeIf(item -> item.getId().equals(productId));
  }

  public void openRestaurant(LocalTime now) {
    if (menu.isEmpty()) {
      throw new InvalidRestaurantOperationException("O restaurante não pode abrir sem um menu.");
    }

    if (isOpen()) {
      throw new InvalidRestaurantOperationException("O restaurante já está aberto.");
    }

    if (!openingHours.isWithin(now)) {
      throw new InvalidRestaurantOperationException("O restaurante não pode abrir fora do horário de funcionamento.");
    }

    this.status = RestaurantStatus.OPEN;
  }

  public void closeRestaurant() {
    this.status = RestaurantStatus.CLOSED;
  }

  public boolean isOpen() {
    return status == RestaurantStatus.OPEN;
  }

  public boolean isClosed() {
    return status == RestaurantStatus.CLOSED;
  }

  public void changeAddress(Address newAddress) {
    this.address = Objects.requireNonNull(newAddress);
  }

  public void changeName(String newName) {
    if (isOpen()) {
      throw new InvalidRestaurantOperationException("Não pode trocar o nome equanto o restaurante está aberto.");
    }

    Objects.requireNonNull(newName);

    if (newName.isBlank()) {
      throw new InvalidRestaurantOperationException("Nome do restaurante não pode ser vazio.");
    }

    this.name = newName;
  }

  public RestaurantId getId() {
    return id;
  }

  public RestaurantStatus getStatus() {
    return status;
  }

  public LocalTime getOpenHour() {
    return openingHours.open();
  }

  public LocalTime getCloseHour() {
    return openingHours.close();
  }

  public List<MenuItem> getMenu() {
    return Collections.unmodifiableList(menu);
  }

  public Address getAddress() {
    return address;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Restaurant)) {
      return false;
    }

    Restaurant restaurant = (Restaurant) obj;
    return id.equals(restaurant.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
