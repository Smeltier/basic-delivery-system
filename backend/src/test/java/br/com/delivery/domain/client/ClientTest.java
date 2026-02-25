package br.com.delivery.domain.client;

import org.junit.jupiter.api.Test;

import br.com.delivery.domain.account.AccountId;
import br.com.delivery.domain.shared.Address;
import br.com.delivery.domain.shared.Cpf;
import br.com.delivery.domain.shared.ZipCode;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
  @Test
  void shouldCreateClient() {
    AccountId id = AccountId.generate();
    Client client = Client.create(id);
    assertEquals(id, client.getId());
  }

  @Test
  void shouldNotCreateClientWithNullId() {
    assertThrows(NullPointerException.class, () -> Client.create(null));
  }

  @Test
  void shouldUpdateAddress() {
    Client client = Client.create(AccountId.generate());
    Address address = new Address("Rua A", "123", "Bairro", "Cidade", "MG", new ZipCode("36703-072"));
    
    client.updateAddress(address);
    
    assertEquals(address, client.getAddress());
  }

  @Test
  void shouldNotUpdateAddressWithNull() {
    Client client = Client.create(AccountId.generate());
    assertThrows(NullPointerException.class, () -> client.updateAddress(null));
  }

  @Test
  void shouldSetCpf() {
    Client client = Client.create(AccountId.generate());
    Cpf cpf = new Cpf("751.960.116-17");
    
    client.setCpf(cpf);
    
    assertTrue(client.getCpf().isPresent());
    assertEquals(cpf, client.getCpf().get());
  }

  @Test
  void shouldNotSetCpfWithNull() {
    Client client = Client.create(AccountId.generate());
    assertThrows(NullPointerException.class, () -> client.setCpf(null));
  }

  @Test
  void shouldBeEqualWhenIdsAreEqual() {
    AccountId id = AccountId.generate();
    Client client1 = Client.restore(id);
    Client client2 = Client.restore(id);
    
    assertEquals(client1, client2);
    assertEquals(client1.hashCode(), client2.hashCode());
  }

  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Client client1 = Client.create(AccountId.generate());
    Client client2 = Client.create(AccountId.generate());
    
    assertNotEquals(client1, client2);
  }
}