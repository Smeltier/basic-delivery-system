package br.com.delivery.infrastructure.web.order;

import java.math.BigDecimal;

public record OrderItemResponse(String menuItemId, int quantity, BigDecimal unitPrice) {
}
