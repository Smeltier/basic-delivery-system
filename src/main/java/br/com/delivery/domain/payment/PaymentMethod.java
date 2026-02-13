package br.com.delivery.domain.payment;

import br.com.delivery.domain.shared.Money;

public interface PaymentMethod {
  void process(Money amount);
}
