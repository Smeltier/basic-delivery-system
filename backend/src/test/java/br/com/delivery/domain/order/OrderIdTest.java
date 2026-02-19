package br.com.delivery.domain.order;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderIdTest {
  @Test
  void shouldCreateOrderIdWithValidUUID() {
    UUID uuid = UUID.randomUUID();
    OrderId id = new OrderId(uuid);

    assertEquals(uuid, id.value());
  }

  @Test
  void shouldThrowExceptionWhenUUIDIsNull() {
    assertThrows(NullPointerException.class,
        () -> new OrderId(null));
  }

  @Test
  void generateShouldReturnNonNullValue() {
    OrderId id = OrderId.generate();

    assertNotNull(id);
    assertNotNull(id.value());
  }

  @Test
  void generateShouldCreateDifferentIds() {
    OrderId id1 = OrderId.generate();
    OrderId id2 = OrderId.generate();

    assertNotEquals(id1, id2);
  }

  @Test
  void twoOrdersWithSameUUIDNeedToBeEquals() {
    UUID id = UUID.randomUUID();
    OrderId id1 = new OrderId(id);
    OrderId id2 = new OrderId(id);

    assertEquals(id1, id2);
  }
}
