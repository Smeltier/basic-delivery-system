package br.com.delivery.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.delivery.application.usecases.order.AddItemToOrderUseCase;
import br.com.delivery.application.usecases.order.RemoveItemFromOrderUseCase;
import br.com.delivery.domain.repositories.IAccountRepository;
import br.com.delivery.domain.repositories.IOrderRepository;
import br.com.delivery.domain.repositories.IRestaurantRepository;

@Configuration
public class ApplicationConfig {
    @Bean
    public IAccountRepository accountRepository() {
    }

    @Bean
    public IRestaurantRepository restaurantRepository() {
    }

    @Bean
    public IOrderRepository orderRepository() {
    }

    @Bean
    public AddItemToOrderUseCase addItemToOrderUseCase(
        IAccountRepository accountRepository,
        IRestaurantRepository restaurantRepository,
        IOrderRepository orderRepository
    ) {
        return new AddItemToOrderUseCase(accountRepository, restaurantRepository, orderRepository);
    }

    @Bean
    public RemoveItemFromOrderUseCase removeItemFromOrderUseCase(
        IOrderRepository orderRepository,
        IRestaurantRepository restaurantRepository
    ) {
        return new RemoveItemFromOrderUseCase(orderRepository, restaurantRepository);
    }
}
