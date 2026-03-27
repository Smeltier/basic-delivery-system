package br.com.delivery.infrastructure.web.dto;

import java.math.BigDecimal;

public record OrderItemResponse(String menuItemId, int quantity, BigDecimal unitPrice) {
}
