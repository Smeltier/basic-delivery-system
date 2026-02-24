package br.com.delivery.domain.item;

import java.util.Objects;

import br.com.delivery.domain.exception.InactiveItemException;
import br.com.delivery.domain.shared.Money;

// TODO: Add RestaurantId.

public class MenuItem {
  private final MenuItemId id;
  private String name;
  private String description;
  private MenuItemCategory category;
  private Money price;
  private boolean active;

  private MenuItem(MenuItemId id, String name, String description, MenuItemCategory category, Money price) {
    this.id = Objects.requireNonNull(id);
    this.price = Objects.requireNonNull(price);
    this.active = true;

    changeName(name);
    changeDescription(description);
    changeCategory(category);
  }

  public static MenuItem create(String name, String description, MenuItemCategory category, Money price) {
    return new MenuItem(MenuItemId.generate(), name, description, category, price);
  }

  public Money currentPrice() {
    assertActive();
    return price;
  }

  public void changePrice(Money newPrice) {
    Objects.requireNonNull(newPrice);
    assertActive();
    this.price = newPrice;
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }

  public boolean isActive() {
    return this.active;
  }

  public MenuItemId getId() {
    return id;
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

  private void assertActive() {
    if (!this.active) {
      throw new InactiveItemException("Item inativo.");
    }
  }
}
