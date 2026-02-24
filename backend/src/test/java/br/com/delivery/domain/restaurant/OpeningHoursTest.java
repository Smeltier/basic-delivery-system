package br.com.delivery.domain.restaurant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

public class OpeningHoursTest {
  @Test
  void shouldCreateValidOpeningHours() {
    LocalTime open = LocalTime.of(7, 0);
    LocalTime close = LocalTime.of(15, 0);
    OpeningHours openingHours = new OpeningHours(open, close);

    assertEquals(open, openingHours.open());
    assertEquals(close, openingHours.close());
  }

  @Test
  void timeBetweenOpenAndCloseShouldBeWithin() {
    LocalTime open = LocalTime.of(7, 0);
    LocalTime close = LocalTime.of(15, 0);
    OpeningHours openingHours = new OpeningHours(open, close);

    LocalTime between = LocalTime.of(12, 0);
    assertTrue(openingHours.isWithin(between));
  }

  @Test
  void shouldThrowWhenCloseIsBeforeOpen() {
    LocalTime open = LocalTime.of(15, 0);
    LocalTime close = LocalTime.of(7, 0);

    assertThrows(IllegalArgumentException.class,
        () -> new OpeningHours(open, close));
  }
}
