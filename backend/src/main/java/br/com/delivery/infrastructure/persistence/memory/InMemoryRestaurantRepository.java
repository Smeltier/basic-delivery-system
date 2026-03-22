package br.com.delivery.infrastructure.persistence.memory;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.repositories.IRestaurantRepository;
import br.com.delivery.domain.restaurant.Restaurant;
import br.com.delivery.domain.restaurant.RestaurantId;

public final class InMemoryRestaurantRepository implements IRestaurantRepository {
    private final Map<UUID, Restaurant> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Restaurant> findById(RestaurantId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public boolean existsById(RestaurantId id) {
        return storage.containsKey(id.value());
    }

    @Override
    public List<Restaurant> findAllByOwnerId(AccountId ownerId) {
        return storage.values().stream()
            .filter(restaurant -> restaurant.getOwnerId().equals(ownerId))
            .toList();
    }

    @Override
    public void save(Restaurant restaurant) {
        storage.put(restaurant.getId().value(), restaurant);
    }
}
