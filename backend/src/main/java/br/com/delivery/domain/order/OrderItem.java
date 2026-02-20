package br.com.delivery.domain.order;

import java.util.Objects;

import br.com.delivery.domain.product.ProductId;
import br.com.delivery.domain.shared.Money;

public class OrderItem {
  private final ProductId productId;
  private final String productName;
  private final Money unitPrice;
  private final int quantity;

  public OrderItem(ProductId productId, String productName, Money unitPrice, int quantity) {
    if (productName.isBlank()) {
      throw new IllegalArgumentException("Nome do produto inválido");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantidade inválida.");
    }
    this.productId = Objects.requireNonNull(productId);
    this.productName = Objects.requireNonNull(productName);
    this.unitPrice = Objects.requireNonNull(unitPrice);
    this.quantity = quantity;
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
