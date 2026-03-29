package br.com.delivery.infrastructure.web.dto;

import java.util.List;
import java.math.BigDecimal;

public record DecreaseItemQuantityFromOrderResponse(String orderId, BigDecimal total, List<OrderItemResponse> items) {
}
