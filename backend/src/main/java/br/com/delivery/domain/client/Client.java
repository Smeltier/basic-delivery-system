package br.com.delivery.domain.client;

import java.util.Objects;
import java.util.Optional;

import br.com.delivery.domain.shared.Cpf;
import br.com.delivery.domain.shared.Email;
import br.com.delivery.domain.exception.InactiveClientException;
import br.com.delivery.domain.exception.InvalidClientOperationException;

public final class Client {
  private final ClientId id;
  private Email email;
  private Cpf cpf;
  private String name;
  private boolean active;

  private Client(final ClientId id, final String name, final Email email) {
    this.id = Objects.requireNonNull(id);
    this.active = true;
    setEmail(email);
    setName(name);
  }

  public static Client create(final String name, final Email email) {
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

  public Optional<Cpf> getCpf() {
    return Optional.ofNullable(this.cpf);
  }

  public void setEmail(final Email newEmail) {
    ensureClientIsActive();
    email = Objects.requireNonNull(newEmail);
  }

  public void setCpf(final Cpf newCpf) {
    ensureClientIsActive();
    this.cpf = Objects.requireNonNull(newCpf);
  }

  public void setName(final String newName) {
    if (newName == null || newName.isBlank()) {
      throw new InvalidClientOperationException("Nome inválido");
    }
    ensureClientIsActive();
    this.name = newName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Client)) {
      return false;
    }
    Client client = (Client) o;
    return this.id.equals(client.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private void ensureClientIsActive() {
    if (!this.active) {
      throw new InactiveClientException("Cliente inativo.");
    }
  }
}
