# Microsserviço de Pedidos

## Descrição
   
O Microsserviço de Pedidos faz parte de um sistema de gerenciamento de pedidos baseado em arquitetura de microsserviços, utilizando o ecossistema Spring. Este serviço centraliza o processamento de todos os pedidos, desde a criação até a finalização.

## Tecnologias Utilizadas

- **[Java 21](https://docs.oracle.com/en/java/javase/21/)**: Linguagem de programação principal.
- **[Spring Boot 3.3.4](https://docs.spring.io/spring-boot/3.3/)**: Framework para a construção de aplicações Java, facilitando o desenvolvimento de microsserviços.
- **[Spring Data JPA](https://spring.io/projects/spring-data-jpa)**: Facilita o gerenciamento de dados e operações CRUD no banco de dados relacional.
- **[Spring Cloud](https://spring.io/projects/spring-cloud)**: Implementa a comunicação assíncrona por meio de mensagens/eventos.
- **MySQL**: Banco de dados relacional utilizado para persistência dos dados de pedidos.
- **Lombok**: Redução de código boilerplate com anotações para getters, setters e outras funcionalidades.
- **OpenAPI (Springdoc)**: Documentação e interface gráfica para explorar as APIs REST.
- **JUnit 5 e Mockito**: Frameworks para testes unitários e mocks.
- **Docker**: Containerização da aplicação para facilitar o deploy em diferentes ambientes.

## Funcionalidades
- Cadastro de pedidos.
- Atualização do status do pedido.
- Consulta de informações de pedidos.
- Cancelamento de pedidos. .
- Comunicação assíncrona.
- Integração com microsserviço de clientes.
- Integração com microsserviço de produtos.\

## Estrutura de pastas do Projeto
- adapter: Classes que convertem objetos de transferência de dados em entidades e vice-versa.
- config: Classes de configuração da aplicação.
- connectors: Classes que implementam a comunicação com outros microsserviços.
- controller: Classes que definem os endpoints da API REST.
  - externo: Classes que definem os endpoints para o cliente.
  - interno: Classes que definem os endpoints entre mocrosserviços.
- exception: Classes que representam exceções personalizadas.
- listener: Classes que implementam listeners para eventos assíncronos.
- model: Classes que representam as entidades do domínio.
- repository: Classes que implementam a comunicação com o banco de dados.
- service: Classes que implementam a lógica de negócio.
  - dto: Classes que representam os objetos de transferência de dados.
  - enums: Classes que representam os tipos enumerados.

## Como Executar o Projeto

### Pré-requisitos

- Java 21+
- Maven
- Docker (para rodar o MySQL e o rabbitmq, opcional)

### Passos

1. Clone o repositório:

   ```bash
   git clone git@github.com:WaldirJuniorGPN/ms-pedido.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd ms-pedidos
   ```
   
3. Certifique-se de que o MySQL e o RabbitMQ estão rodando. Caso prefira, utilize Docker para rodar os containers:

   ```bash
   docker-compose up -d
   ```


4. Baixe as dependências do projeto:

   ```bash
   mvn clean install
   ```

5. Execute o projeto com o Maven:

   ```bash
   mvn spring-boot:run
   ```

## Testes

O microsserviço de pedidos possui testes automatizados para validar as funcionalidades principais, tanto em nível unitário quanto de integração.

### Executar os Testes

```bash
mvn test
```

