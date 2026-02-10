package br.com.delivery.domain.order;

import br.com.delivery.domain.Product;
import br.com.delivery.domain.shared.Money;

public class OrderItem {
  private final int quantity;
  private final Product product;
  private final Money unitPrice;

  public OrderItem(Product product, int quantity, Money unitPrice) {
    if (product == null) {
      throw new IllegalArgumentException("Produto não pode ser nulo.");
    }
    if (unitPrice == null) {
      throw new IllegalArgumentException("Preço unitário não poder ser nulo.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantidade inválida.");
    }
    this.product = product;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public Money total() {
    return unitPrice.multiply(quantity);
  }
}
