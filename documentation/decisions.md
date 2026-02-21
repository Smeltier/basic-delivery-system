# Architectural Decisions

Este documento registra decisões técnicas do projeto.

---

## ADR-001 - Uso de DDD

### Decisão

Adotar princípios de Domain-Driven Design para modelagem do sistema.

### Motivação

Separar claramente regras de negócio de detalhes de infraestrutura.

### Consequência

Maior organização e facilidade de evolução futura.

---

## ADR-002 - Uso de Value Objects imutáveis

### Decisão

Representar conceitos como Money e Email como objetos imutáveis

### Motivação

Evitar estados inválidos e melhorar consistência do domínio.

### Consequência

Maior segurança e previsibilidade do sistema.

---

## ADR-003 - Repositórios como interfaces no domínio

### Decisão

Definir contratos de persistência no domínio e implementações na infraestrutura.

### Motivação

Permitir múltiplas estratégias de armazenamento (InMemory, JPA, etc).

### Consequência

Flexibilidade e testabilidade.

---

## ADR-004 - Uso de Enum para o status do pagamento ao invés de State Pattern

### Decisão

Usar um enum `PaymentStatus` dentro do agregado `Payment` ao invés de múltiplas classes implementando `PaymentState`.

### Motivação

O ciclo de vida do pagamento é simples e não possui comportamentos específicos por estado que justifiquem a complexidade do State Pattern.

### Consequência

Código mais simples, com menos classes e manutenção mais direta das regras de transição. Caso a lógica de cada estado evolua, o padrão State poderá ser reavaliado.

## ADR-005 - Opcionalidade de CPF no agregado Client

### Decisão

Não obrigar adicionar o CPF no momento da criação de um cliente.

### Motivação

O CPF é usado principalmente pelos métodos de pagamento. Caso o cliente não faça uma compra em um método que use o CPF, o dado será inútil. Muitos sistemas de entrega não obrigam adicionar o CPF no momento da criação da conta.

### Consequência

A validação da presença do CPF deve ser movida para o Checkout ou para o Contexto de Pagamento. O sistema deve ser capaz de lidar com um objeto Client que possui Cpf nulo, e a interface deve solicitar o dado apenas quando o método de pagamento selecionado o exigir.

É possível que o cliente desista de uma compra ao ver a necessidade do CPF tardio.
