package br.com.delivery.domain.order;

import java.util.Objects;

import br.com.delivery.domain.item.MenuItemCategory;
import br.com.delivery.domain.item.MenuItemId;
import br.com.delivery.domain.shared.Money;
import br.com.delivery.domain.exception.InvalidOrderItemException;
import br.com.delivery.domain.exception.InvalidOrderItemQuantityException;

public class OrderItem {
  private final MenuItemId menuItemId;
  private final String menuItemName;
  private final String menuItemDescription;
  private final Money unitPrice;
  private final int quantity;

  public OrderItem(MenuItemId menuItemId, String menuItemName, String menuItemDescription, MenuItemCategory category,
      Money unitPrice, int quantity) {
    Objects.requireNonNull(menuItemId);
    Objects.requireNonNull(menuItemName);
    Objects.requireNonNull(menuItemDescription);
    Objects.requireNonNull(category);
    Objects.requireNonNull(unitPrice);

    if (menuItemName.isBlank()) {
      throw new InvalidOrderItemException("Nome não pode ser vazio.");
    }

    if (menuItemDescription.isBlank()) {
      throw new InvalidOrderItemException("Descrição não pode ser vazia.");
    }

    if (quantity <= 0) {
      throw new InvalidOrderItemQuantityException("Quantidade inválida.");
    }

    this.menuItemId = menuItemId;
    this.menuItemName = menuItemName;
    this.menuItemDescription = menuItemDescription;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
  }

  public Money total() {
    return unitPrice.multiply(quantity);
  }

  public MenuItemId getMenuItemId() {
    return menuItemId;
  }

  public String getMenuItemName() {
    return menuItemName;
  }

  public String getMenuItemDescription() {
    return menuItemDescription;
  }

  public Money getUnitPrice() {
    return unitPrice;
  }

  public int getQuantity() {
    return quantity;
  }
}
