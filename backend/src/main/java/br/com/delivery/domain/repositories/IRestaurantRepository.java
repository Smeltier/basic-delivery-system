package br.com.delivery.domain.repositories;

import java.util.List;
import java.util.Optional;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.restaurant.Restaurant;
import br.com.delivery.domain.restaurant.RestaurantId;

public interface IRestaurantRepository {
  Optional<Restaurant> findById(RestaurantId id);

  List<Restaurant> findAllByOwnerId(AccountId ownerId);

  boolean existsById(AccountId id);

  void save(Restaurant restaurant);
}