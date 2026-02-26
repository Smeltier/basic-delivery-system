package br.com.delivery.domain.account;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.com.delivery.domain.shared.Email;
import br.com.delivery.domain.exception.InactiveAccountException;
import br.com.delivery.domain.exception.InvalidClientException;

public final class Account {
  private final AccountId id;
  private final Set<AccountRole> roles;
  private boolean active;
  private String name;
  private Email email;

  private Account(AccountId id, String name, Email email, Set<AccountRole> roles) {
    this.id = Objects.requireNonNull(id);
    this.roles = new HashSet<>(roles);
    this.active = true;
    changeEmail(email);
    changeName(name);
  }

  public static Account create(String name, Email email, Set<AccountRole> roles) {
    return new Account(AccountId.generate(), name, email, roles);
  }

  public static Account restore(AccountId id, String name, Email email, Set<AccountRole> roles) {
    Account account = new Account(id, name, email, roles);
    return account;
  }

  public void changeEmail(Email newEmail) {
    ensureAccountIsActive();
    email = Objects.requireNonNull(newEmail);
  }

  public void changeName(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new InvalidClientException("Nome inválido");
    }
    ensureAccountIsActive();
    this.name = newName;
  }

  public void addRole(AccountRole newRole) {
    ensureAccountIsActive();
    this.roles.add(Objects.requireNonNull(newRole));
  }

  public void assertCanPlaceOrder() {
    if (!this.active) {
      throw new InactiveAccountException("Conta inativa");
    }
  }

  public boolean isActive() {
    return this.active;
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }

  public String getName() {
    return name;
  }

  public AccountId getId() {
    return id;
  }

  public Email getEmail() {
    return email;
  }

  public Set<AccountRole> getRoles() {
    return Collections.unmodifiableSet(roles);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Account)) {
      return false;
    }
    Account account = (Account) o;
    return this.id.equals(account.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private void ensureAccountIsActive() {
    if (!this.active) {
      throw new InactiveAccountException("Conta inativa.");
    }
  }
}
