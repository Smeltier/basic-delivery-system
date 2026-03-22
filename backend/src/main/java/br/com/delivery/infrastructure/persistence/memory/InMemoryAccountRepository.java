package br.com.delivery.infrastructure.persistence.memory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.UUID;

import br.com.delivery.domain.account.Account;
import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.repositories.IAccountRepository;

public final class InMemoryAccountRepository implements IAccountRepository {
    private final Map<UUID, Account> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(AccountId id) {
        return Optional.ofNullable(storage.get(id.value()));
    }

    @Override
    public void save(Account account) {
        storage.put(account.getId().value(), account);
    }
}
