# Microsserviço de Pedidos

## Descrição
   
O Microsserviço de Pedidos faz parte de um sistema de gerenciamento de pedidos baseado em arquitetura de microsserviços, utilizando o ecossistema Spring. Este serviço centraliza o processamento de todos os pedidos, desde a criação até a finalização.

## Funcionalidades

- **CRUD de Pedidos**: Gerencia operações de criação, leitura e atualização de pedidos.
- **Persistência de Dados**: Utiliza Spring Data JPA para manipulação de dados de pedidos em um banco de dados relacional.
- **Comunicação Assíncrona**: Usa Spring Cloud Stream para comunicação baseada em eventos com outros microsserviços.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento e configuração do microsserviço.
- **Spring Data JPA**: Facilita o gerenciamento de dados e operações CRUD no banco de dados relacional.
- **Spring Cloud Stream**: Implementa a comunicação assíncrona com outros serviços por meio de mensagens/eventos.
  
## Como Executar o Projeto

### Pré-requisitos

- Java 21+
- Docker (opcional, se preferir usar contêineres para o banco de dados)

### Passos

1. Clone o repositório:

   ```bash
   git clone git@github.com:WaldirJuniorGPN/ms-pedido.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd ms-pedidos
   ```

3. Execute o projeto com o Maven:

   ```bash
   mvn spring-boot:run
   ```

4. Se preferir, utilize Docker para rodar o banco de dados relacional (MySQL, por exemplo).

## Testes

O microsserviço de pedidos possui testes automatizados para validar as funcionalidades principais, tanto em nível unitário quanto de integração.

### Executar os Testes

```bash
mvn test
```

