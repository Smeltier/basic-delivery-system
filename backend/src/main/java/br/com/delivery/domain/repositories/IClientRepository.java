package br.com.delivery.domain.repositories;

import java.util.Optional;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.client.Client;

public interface IClientRepository {
  Optional<Client> findById(AccountId id);

  void save(Client client);
}