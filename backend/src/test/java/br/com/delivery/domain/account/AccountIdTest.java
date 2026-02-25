package br.com.delivery.domain.account;

import org.junit.jupiter.api.Test;

import br.com.delivery.domain.exception.InactiveAccountException;
import br.com.delivery.domain.exception.InvalidClientException;
import br.com.delivery.domain.shared.Email;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class AccountIdTest { 
  @Test
  void shouldCreateAccount() {
    Account account = Account.create("Teste", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT));
    
    assertTrue(account.isActive());
    assertEquals("Teste", account.getName());
    assertEquals(1, account.getRoles().size());
  }

  @Test
  void shouldNotCreateWithInvalidName() {
    assertThrows(InvalidClientException.class, () -> 
        Account.create("", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT))
    );
  }

  @Test
  void shouldChangeEmail() {
    Account account = Account.create("Teste", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT));
    Email newEmail = new Email("novo@email.com");
    
    account.changeEmail(newEmail);
    
    assertEquals(newEmail, account.getEmail());
  }

  @Test
  void shouldAddRole() {
    Account account = Account.create("Teste", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT));
    
    account.addRole(AccountRole.RESTAURANT_OWNER);
    
    assertEquals(2, account.getRoles().size());
    assertTrue(account.getRoles().contains(AccountRole.RESTAURANT_OWNER));
  }

  @Test
  void shouldThrowWhenModifyingInactiveAccount() {
    Account account = Account.create("Teste", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT));
    account.deactivate();
    
    assertThrows(InactiveAccountException.class, () -> account.changeName("Novo Nome"));
    assertThrows(InactiveAccountException.class, () -> account.changeEmail(new Email("novo@email.com")));
    assertThrows(InactiveAccountException.class, () -> account.addRole(AccountRole.RESTAURANT_OWNER));
  }

  @Test
  void shouldActivateAndDeactivate() {
    Account account = Account.create("Teste", new Email("teste@email.com"), Set.of(AccountRole.BASE_CLIENT));
    
    account.deactivate();
    assertFalse(account.isActive());
    
    account.activate();
    assertTrue(account.isActive());
  }

  @Test
  void shouldBeEqualWhenIdsAreEqual() {
    AccountId id = AccountId.generate();
    Account account1 = Account.restore(id, "Teste 1", new Email("teste1@email.com"), Set.of(AccountRole.BASE_CLIENT));
    Account account2 = Account.restore(id, "Teste 2", new Email("teste2@email.com"), Set.of(AccountRole.BASE_CLIENT));
    
    assertEquals(account1, account2);
    assertEquals(account1.hashCode(), account2.hashCode());
  }
  
  @Test
  void shouldNotBeEqualWhenIdsAreDifferent() {
    Account account1 = Account.create("Teste 1", new Email("teste1@email.com"), Set.of(AccountRole.BASE_CLIENT));
    Account account2 = Account.create("Teste 2", new Email("teste2@email.com"), Set.of(AccountRole.BASE_CLIENT));
    
    assertNotEquals(account1, account2);
  }
}