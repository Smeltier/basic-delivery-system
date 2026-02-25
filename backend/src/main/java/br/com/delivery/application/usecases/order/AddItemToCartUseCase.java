package br.com.delivery.application.usecases.order;

import java.util.Objects;

import br.com.delivery.application.dto.order.AddItemToCartInput;
import br.com.delivery.application.dto.order.CreateOrderOutput;
import br.com.delivery.application.exceptions.AccountNotFoundException;
import br.com.delivery.application.exceptions.MenuItemNotFoundException;
import br.com.delivery.application.exceptions.RestaurantNotFoundException;
import br.com.delivery.domain.account.Account;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.order.Order;
import br.com.delivery.domain.repositories.IAccountRepository;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.repositories.IRestaurantRepository;
import br.com.delivery.domain.restaurant.MenuItem;
import br.com.delivery.domain.restaurant.MenuItemId;
import br.com.delivery.domain.restaurant.Restaurant;
import br.com.delivery.domain.restaurant.RestaurantId;

public final class AddItemToCartUseCase {
  private IAccountRepository accountRepository;
  private IRestaurantRepository restaurantRepository;
  private IOrderRepository orderRepository;

  public AddItemToCartUseCase(IAccountRepository accountRepository, IRestaurantRepository restaurantRepository, IOrderRepository orderRepository) {
    this.accountRepository = Objects.requireNonNull(accountRepository);
    this.restaurantRepository = Objects.requireNonNull(restaurantRepository);
    this.orderRepository = Objects.requireNonNull(orderRepository);
  }

  public CreateOrderOutput execute(AddItemToCartInput input) {
    AccountId accountId = input.accountId();
    RestaurantId restaurantId = input.restaurantId();
    MenuItemId menuItemId = input.menuItemId();
    int quantity = input.quantity();

    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new AccountNotFoundException("Cliente não encontrado."));
    account.assertCanPlaceOrder();

    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new RestaurantNotFoundException("Restaurante não encontrado."));

    Order order = orderRepository.findDraftByClientAndRestaurant(accountId, restaurantId)
        .orElse(Order.create(restaurantId, accountId, restaurant.getCurrency()));

    MenuItem item = restaurant.findMenuItem(menuItemId)
        .orElseThrow(() -> new MenuItemNotFoundException("Item não encontrado."));
    item.assertActive();

    order.addItem(menuItemId, item.getName(), item.getDescription(), item.getCategory(), item.currentPrice(), quantity);
    
    orderRepository.save(order);
    return new CreateOrderOutput(order.getId());
  }
}