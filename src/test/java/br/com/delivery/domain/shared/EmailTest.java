package br.com.delivery.domain.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {
  @Test
  void shouldNotBeAbleToCreateWithBlankValid() {
    assertThrows(IllegalArgumentException.class,
        () -> new Email(""));
  }
}
