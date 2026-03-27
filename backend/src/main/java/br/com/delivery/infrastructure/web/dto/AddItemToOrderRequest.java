package br.com.delivery.infrastructure.web.dto;

public record AddItemToOrderRequest(String accountId, String restaurantId, String menuItemId, int quantity) {
}
