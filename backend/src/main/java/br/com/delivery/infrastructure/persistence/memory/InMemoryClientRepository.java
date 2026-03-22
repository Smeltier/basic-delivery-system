package br.com.delivery.infrastructure.persistence.memory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.client.Client;
import br.com.delivery.domain.repositories.IClientRepository;

public final class InMemoryClientRepository implements IClientRepository {
    private final Map<UUID, Client> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Client> findById(AccountId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public boolean existsById(AccountId id) {
        return storage.containsKey(id.value());
    }

    @Override
    public void save(Client client) {
        storage.put(client.getId().value(), client);
    }
}
