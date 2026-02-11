package br.com.delivery.domain.order;

public class PaidState implements OrderState {
  @Override
  public void addItem(Order order, OrderItem item) {
    throw new InvalidOrderOperationException(
        "Não se pode adicionar novos itens a um pedido já pago.");
  }

  @Override
  public void pay(Order order) {
    throw new InvalidOrderOperationException(
        "Não se pode pagar novamente um pedido que já foi pago.");
  }

  @Override
  public void cancel(Order order) {
    throw new InvalidOrderOperationException(
        "Não se pode cancelar um pedido que já foi pago.");
  }
}
