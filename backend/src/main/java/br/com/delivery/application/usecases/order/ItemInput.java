// package br.com.delivery.application.usecases.order;
//
// import br.com.delivery.domain.product.ProductId;
//
// import java.util.Objects;
//
// public record ItemInput(ProductId productId, int quantity) {
// public ItemInput {
// if (quantity < 0) {
// throw new IllegalArgumentException("Quantidade não pode ser negativa.");
// }
// Objects.requireNonNull(productId);
// }
// }
