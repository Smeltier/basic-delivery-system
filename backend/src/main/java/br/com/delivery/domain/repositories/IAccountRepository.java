package br.com.delivery.domain.repositories;

import java.util.Optional;

import br.com.delivery.domain.account.Account;
import br.com.delivery.domain.account.AccountId;

public interface IAccountRepository {  
  Optional<Account> findById(AccountId id);

  void save(Account account); 
}