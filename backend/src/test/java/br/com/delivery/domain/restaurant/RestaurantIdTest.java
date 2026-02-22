package br.com.delivery.domain.restaurant;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantIdTest {
  @Test
  void shouldCreateOrderIdWithValidUUID() {
    UUID uuid = UUID.randomUUID();
    RestaurantId id = new RestaurantId(uuid);

    assertEquals(uuid, id.value());
  }

  @Test
  void shouldThrowExceptionWhenUUIDIsNull() {
    assertThrows(NullPointerException.class,
        () -> new RestaurantId(null));
  }

  @Test
  void generateShouldReturnNonNullValue() {
    RestaurantId id = RestaurantId.generate();

    assertNotNull(id);
    assertNotNull(id.value());
  }

  @Test
  void generateShouldCreateDifferentIds() {
    RestaurantId id1 = RestaurantId.generate();
    RestaurantId id2 = RestaurantId.generate();

    assertNotEquals(id1, id2);
  }

  @Test
  void twoOrdersWithSameUUIDNeedToBeEquals() {
    UUID id = UUID.randomUUID();
    RestaurantId id1 = new RestaurantId(id);
    RestaurantId id2 = new RestaurantId(id);

    assertEquals(id1, id2);
  }
}
