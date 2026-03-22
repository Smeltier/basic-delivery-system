package br.com.delivery.infrastructure.web.order;

public record AddItemToOrderRequest(String accountId, String restaurantId, String menuItemId, int quantity) {
}
