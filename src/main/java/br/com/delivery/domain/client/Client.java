package br.com.delivery.domain.client;

import br.com.delivery.domain.shared.Email;

public class Client {
  private final ClientId id;
  private Email email;
  private String name;
  private boolean active;

  public Client(final ClientId id, final String name, final Email email) {
    if (id == null) {
      throw new IllegalArgumentException("ID não pode ser nulo.");
    }
    this.id = id;
    this.active = true;

    setEmail(email);
    setName(name);
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
    if (newEmail == null) {
      throw new IllegalArgumentException("Email inválido.");
    }
    this.email = newEmail;
  }

  public void setName(String newName) {
    if (newName == null || newName.isBlank()) {
      throw new IllegalArgumentException("Nome inválido");
    }
    this.name = newName;
  }
}
