package br.com.delivery.domain.restaurant;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenuItemIdTest {
  @Test
  void shouldCreateOrderIdWithValidUUID() {
    UUID uuid = UUID.randomUUID();
    MenuItemId id = new MenuItemId(uuid);

    assertEquals(uuid, id.value());
  }

  @Test
  void shouldThrowExceptionWhenUUIDIsNull() {
    assertThrows(NullPointerException.class,
        () -> new MenuItemId(null));
  }

  @Test
  void generateShouldReturnNonNullValue() {
    MenuItemId id = MenuItemId.generate();

    assertNotNull(id);
    assertNotNull(id.value());
  }

  @Test
  void generateShouldCreateDifferentIds() {
    MenuItemId id1 = MenuItemId.generate();
    MenuItemId id2 = MenuItemId.generate();

    assertNotEquals(id1, id2);
  }

  @Test
  void twoOrdersWithSameUUIDNeedToBeEquals() {
    UUID id = UUID.randomUUID();
    MenuItemId id1 = new MenuItemId(id);
    MenuItemId id2 = new MenuItemId(id);

    assertEquals(id1, id2);
  }
}
