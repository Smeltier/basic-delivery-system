package br.com.delivery.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record RemoveItemFromOrderResponse(String orderId, BigDecimal total, List<OrderItemResponse> items) {
}
