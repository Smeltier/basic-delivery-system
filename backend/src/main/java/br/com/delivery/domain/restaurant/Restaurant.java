package br.com.delivery.domain.restaurant;

import java.time.LocalTime;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.exception.InvalidRestaurantException;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Money;

public final class Restaurant {
  private final RestaurantId id;
  private final AccountId ownerId;
  private final List<MenuItem> menu;
  private String name;
  private RestaurantStatus status;
  private OpeningHours openingHours;
  private Address address;

  private Restaurant(RestaurantId id, AccountId ownerId, String name, OpeningHours openingHours, Address address) {
    this.id = Objects.requireNonNull(id);
    this.ownerId = Objects.requireNonNull(ownerId);
    this.openingHours = Objects.requireNonNull(openingHours);
    this.status = RestaurantStatus.CLOSED;
    this.menu = new ArrayList<>();

    changeAddress(address);
    changeName(name);
  }

  public static Restaurant create(AccountId ownerId, String name, OpeningHours openingHours, Address address) {
    return new Restaurant(RestaurantId.generate(), ownerId, name, openingHours, address);
  }

  public static Restaurant restore(RestaurantId id, AccountId ownerId, String name, OpeningHours openingHours, Address address,
      RestaurantStatus status, List<MenuItem> menu) {
    Restaurant restaurant = new Restaurant(id, ownerId, name, openingHours, address);
    restaurant.status = status;
    restaurant.menu.addAll(menu);
    return restaurant;
  }

  public void addMenuItem(String itemName, String itemDescription, MenuItemCategory category, Money unitPrice) {
    if (status != RestaurantStatus.CLOSED) {
      throw new InvalidRestaurantException("Não pode adicionar itens com o restaurante ainda aberto.");
    }

    if (itemName == null) {
      throw new InvalidRestaurantException("Nome do produto não deve ser nulo.");
    }

    if (unitPrice == null) {
      throw new InvalidRestaurantException("Preço unitário do produto não deve ser nulo.");
    }

    MenuItem item = new MenuItem(MenuItemId.generate(), id, itemName, itemDescription, category, unitPrice);
    this.menu.add(item);
  }

  public void removeMenuItem(MenuItemId productId) {
    if (status != RestaurantStatus.CLOSED) {
      throw new InvalidRestaurantException("Não pode remover itens com o restaurante ainda aberto.");
    }

    menu.removeIf(item -> item.getId().equals(productId));
  }

  public void openRestaurant(LocalTime now) {
    if (menu.isEmpty()) {
      throw new InvalidRestaurantException("O restaurante não pode abrir sem um menu.");
    }

    if (isOpen()) {
      throw new InvalidRestaurantException("O restaurante já está aberto.");
    }

    if (!openingHours.isWithin(now)) {
      throw new InvalidRestaurantException("O restaurante não pode abrir fora do horário de funcionamento.");
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
      throw new InvalidRestaurantException("Não pode trocar o nome equanto o restaurante está aberto.");
    }

    Objects.requireNonNull(newName);

    if (newName.isBlank()) {
      throw new InvalidRestaurantException("Nome do restaurante não pode ser vazio.");
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

  public AccountId getOwnerId() {
    return ownerId;
  }

  public OpeningHours getOpeningHours() {
    return openingHours;
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
