package br.com.delivery.domain.order;

import br.com.delivery.domain.Product;
import br.com.delivery.domain.ProductId;
import br.com.delivery.domain.shared.Money;

public class OrderItem {
  private final ProductId productId;
  private final String productName;
  private final Money unitPrice;
  private final int quantity;

  public OrderItem(ProductId productId, String productName, Money unitPrice, int quantity) {
    if (productId == null) {
      throw new IllegalArgumentException("ID do produto não pode ser nulo.");
    }
    if (productName == null || productName.isBlank()) {
      throw new IllegalArgumentException("Nome do produto inválido");
    }
    if (unitPrice == null) {
      throw new IllegalArgumentException("Preço unitário não poder ser nulo.");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantidade inválida.");
    }
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public Money total() {
    return unitPrice.multiply(quantity);
  }

  public ProductId getProductId() {
    return productId;
  }

  public String getProductName() {
    return productName;
  }

  public Money getUnitPrice() {
    return unitPrice;
  }

  public int getQuantity() {
    return quantity;
  }
}
