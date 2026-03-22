package br.com.delivery.infrastructure.web.order;

import java.math.BigDecimal;
import java.util.List;

public record RemoveItemFromOrderResponse(String orderId, BigDecimal total, List<OrderItemResponse> items) {
}
