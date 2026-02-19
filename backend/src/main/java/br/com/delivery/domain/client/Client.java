package br.com.delivery.domain.client;

import java.util.Objects;

import br.com.delivery.domain.shared.Email;
import br.com.delivery.domain.exception.InactiveClientException;
import br.com.delivery.domain.exception.InvalidClientOperationException;

public class Client {
  private final ClientId id;
  private Email email;
  private String name;
  private boolean active;

  private Client(ClientId id, String name, Email email) {
    this.id = Objects.requireNonNull(id);
    this.active = true;
    setEmail(email);
    setName(name);
  }

  public static Client create(String name, Email email) {
    return new Client(ClientId.generate(), name, email);
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

  public ClientId getId() {
    return id;
  }

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email newEmail) {
    ensureClientIsActive();
    email = Objects.requireNonNull(newEmail);
  }

  public void setName(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new InvalidClientOperationException("Nome inválido");
    }
    ensureClientIsActive();
    this.name = newName;
  }

  private void ensureClientIsActive() {
    if (!this.active) {
      throw new InactiveClientException("Cliente inativo.");
    }
  }
}
