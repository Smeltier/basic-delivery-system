package br.com.delivery.domain.shared;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

  public Email {
    Objects.requireNonNull(value);

    if (value.isBlank()) {
      throw new IllegalArgumentException("Email não pode ser vazio.");
    }

    if (!EMAIL_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException("Email inválido.");
    }

    value = value.toLowerCase();
  }
}
