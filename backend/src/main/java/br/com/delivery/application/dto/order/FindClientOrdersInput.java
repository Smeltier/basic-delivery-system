package br.com.delivery.application.dto.order;

import java.util.Objects;

import br.com.delivery.domain.account.AccountId;

public record FindClientOrdersInput(AccountId accountId) {
  public FindClientOrdersInput {
    accountId = Objects.requireNonNull(accountId);
  }
}
