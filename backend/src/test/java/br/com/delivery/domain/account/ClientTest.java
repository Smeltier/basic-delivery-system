package br.com.delivery.domain.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import br.com.delivery.domain.shared.Email;
import br.com.delivery.domain.exception.InvalidClientException;
import br.com.delivery.domain.client.Client;
import br.com.delivery.domain.exception.InactiveAccountException;

public class ClientTest {
  private final Email email = new Email("test@gmail.com");

  @Test
  void shouldStartActive() {
    Client client = Client.create("Name", this.email);
    assertTrue(client.isActive());
  }

  @Test
  void shouldThrowWithNullEmail() {
    assertThrows(NullPointerException.class,
        () -> Client.create("name", null));
  }

  @Test
  void shouldThrowWithBlankName() {
    assertThrows(InvalidClientException.class,
        () -> Client.create("", this.email));
  }

  @Test
  void shouldThrowWhenSetNameInInactiveClient() {
    Client client = Client.create("name", this.email);
    client.deactivate();

    assertThrows(InactiveAccountException.class,
        () -> client.setName("George"));
  }

  @Test
  void shouldThrowWhenSetEmailIninactiveClient() {
    Client client = Client.create("name", this.email);
    client.deactivate();

    assertThrows(InactiveAccountException.class,
        () -> client.setEmail(new Email("novo@gmail.com")));
  }
}
