package br.com.delivery.infrastructure.persistence.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Optional;
import java.util.UUID;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.repositories.IRestaurantOwnerRepository;
import br.com.delivery.domain.restaurantowner.RestaurantOwner;

public final class InMemoryRestaurantOwnerRepository implements IRestaurantOwnerRepository {
    private final Map<UUID, RestaurantOwner> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<RestaurantOwner> findById(AccountId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public boolean existsById(AccountId id) {
        return storage.containsKey(id.value());
    }

    @Override
    public void save(RestaurantOwner restaurantOwner) {
        storage.put(restaurantOwner.getId().value(), restaurantOwner);
    }
}
