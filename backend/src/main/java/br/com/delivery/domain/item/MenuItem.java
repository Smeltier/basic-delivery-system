package br.com.delivery.domain.item;

import java.util.Objects;

import br.com.delivery.domain.exception.InactiveItemException;
import br.com.delivery.domain.restaurant.RestaurantId;
import br.com.delivery.domain.shared.Money;

public final class MenuItem {
  private final MenuItemId id;
  private final RestaurantId restaurantId;
  private String name;
  private String description;
  private MenuItemCategory category;
  private Money price;
  private boolean active;

  public MenuItem(MenuItemId id, RestaurantId restaurantId, String name, String description, MenuItemCategory category,
      Money price) {
    this.id = Objects.requireNonNull(id);
    this.restaurantId = Objects.requireNonNull(restaurantId);
    this.price = Objects.requireNonNull(price);
    this.active = true;

    changeName(name);
    changeDescription(description);
    changeCategory(category);
  }

  public Money currentPrice() {
    return price;
  }

  public void changePrice(Money newPrice) {
    Objects.requireNonNull(newPrice);
    assertActive();
    this.price = newPrice;
  }

  public void changeName(String newName) {
    Objects.requireNonNull(newName);
    if (newName.isBlank()) {
      throw new IllegalArgumentException("Nome inválido.");
    }
    this.name = newName;
  }

  public void changeDescription(String newDescription) {
    Objects.requireNonNull(newDescription);
    if (newDescription.isBlank()) {
      throw new IllegalArgumentException("Descrição inválida.");
    }
    this.description = newDescription;
  }

  public void changeCategory(MenuItemCategory newCategory) {
    this.category = Objects.requireNonNull(newCategory);
  }

  public void activate() {
    this.active = true;
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() {
    return this.active;
  }

  public MenuItemId getId() {
    return id;
  }

  public RestaurantId getRestaurantId() {
    return restaurantId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public MenuItemCategory getCategory() {
    return category;
  }

  private void assertActive() {
    if (!this.active) {
      throw new InactiveItemException("Item inativo.");
    }
  }
}
