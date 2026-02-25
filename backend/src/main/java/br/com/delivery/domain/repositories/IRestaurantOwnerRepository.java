package br.com.delivery.domain.repositories;

import java.util.Optional;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.restaurantowner.RestaurantOwner;

public interface IRestaurantOwnerRepository {
  Optional<RestaurantOwner> findById(AccountId id);

  boolean existsById(AccountId id);

  void save(RestaurantOwner restaurantOwner);
}