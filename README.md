# 📦 noWaste

Sistema backend em Java voltado ao **controle de estoque por lote e validade**, criado para ajudar empresas a organizar inventários, acompanhar vencimentos e reduzir perdas com produtos expirados.

A aplicação foi desenvolvida com **Spring Boot**, **JPA/Hibernate**, **MySQL** e **JWT**, disponibilizando uma API REST para autenticação, gerenciamento de inventários, produtos, lotes e consultas de alertas. O foco do projeto é oferecer uma base funcional, clara e escalável para controle de estoque em diferentes tipos de negócio.

---

## ✨ O que é o noWaste?

O **noWaste** surgiu para resolver um problema comum em operações com estoque: a dificuldade de acompanhar a validade dos produtos de forma organizada.

Em vez de controlar apenas produtos de forma genérica, o sistema trabalha com **lotes**, o que permite saber:

- qual produto está armazenado
- em qual inventário ele está
- qual a quantidade disponível
- quando esse lote vence
- qual o status atual da validade

O fluxo principal da aplicação segue esta estrutura:

```text
Usuário → Inventário → Produto → Lote
```

---

## 🎯 Finalidade do projeto

O sistema busca apoiar negócios que precisam de um controle mais eficiente sobre mercadorias armazenadas, principalmente quando existe risco de perda por vencimento.

### Problemas que o projeto ajuda a resolver

- dificuldade para controlar validade por lote
- falta de organização dos produtos no estoque
- desperdício causado por vencimentos não acompanhados
- pouca visibilidade sobre produtos próximos da data limite

### Público-alvo

- supermercados
- mercearias
- pequenos comércios
- negócios que trabalham com estoque de produtos perecíveis ou com data de validade

---

## 👨‍💻 Equipe

| Integrante | Função |
|---|---|
| Gabriel Felipe | Product Owner |
| Isadora Rodrigues | Frontend |
| Wesley Carvalho | Scrum Master |
| Henrique Cezar | Backend |
| Gabrielly dos Santos | Frontend |

---

## 🧰 Stack utilizada

### Desenvolvimento backend
- Java 21
- Spring Boot 4.0.4
- Spring Web
- Spring Data JPA
- Spring Security

### Persistência e banco
- MySQL
- Hibernate
- H2 (presente no projeto para apoio em desenvolvimento/testes)

### Bibliotecas e apoio
- Lombok
- ModelMapper
- Java JWT
- JUnit 5
- Mockito
- Maven

---

## 🚀 Primeiros passos

Antes de executar a aplicação, tenha instalado:

| Ferramenta | Requisito                    |
|---|------------------------------|
| Java | JDK 21                       |
| Maven | compatível com o projeto     |
| MySQL | ambiente local               |
| Git | versão atual                 |
| IDE | IntelliJ, Eclipse ou VS Code |

### Conferindo a instalação

```bash
java -version
mvn -version
mysql --version
```

---

## ⚙️ Como configurar o ambiente

### 1. Clonar o repositório

```bash
git clone https://github.com/gabszinn/Nowaste.git
cd Nowaste/noWaste
```

### 2. Criar o arquivo `.env`

A aplicação utiliza variáveis externas de configuração. Crie o arquivo `.env` com os dados do seu ambiente:

```properties
TOKEN_SECRET=seu_token_secret
MYSQL_PORT_KEY=3306
MYSQL_DATABASE_NAME=nome_do_banco
MYSQL_USERNAME_KEY=seu_usuario
MYSQL_PASSWORD_KEY=sua_senha
```

### 3. Configuração do Spring

O projeto já está preparado para usar o perfil local.

Trecho do `application.properties`:

```properties
spring.application.name=noWaste
spring.profiles.active=local
spring.config.import=optional:file:.env[.properties],optional:file:../.env[.properties]
```

Trecho do `application-local.properties`:

```properties
token.secret=${TOKEN_SECRET}

spring.datasource.url=jdbc:mysql://localhost:${MYSQL_PORT_KEY}/${MYSQL_DATABASE_NAME}
spring.datasource.username=${MYSQL_USERNAME_KEY}
spring.datasource.password=${MYSQL_PASSWORD_KEY}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## ▶️ Como executar

### Windows

```bash
.\mvnw.cmd spring-boot:run
```

### Linux/macOS

```bash
./mvnw spring-boot:run
```

---

## 🧱 Organização do projeto

```
noWaste/
├── pom.xml                               # dependências e configurações do Maven
├── mvnw                                  # Maven Wrapper (Linux/macOS)
├── mvnw.cmd                              # Maven Wrapper (Windows)
├── src/
│   ├── main/
│   │   ├── java/A3/project/noWaste/
│   │   │   ├── NoWasteApplication.java   # classe principal da aplicação
│   │   │   ├── config/                   # configurações gerais, segurança e JWT
│   │   │   ├── domain/                   # entidades do sistema
│   │   │   ├── dto/                      # objetos de entrada e saída
│   │   │   │   ├── requests/             # DTOs de requisição
│   │   │   │   └── responses/            # DTOs de resposta
│   │   │   ├── exceptions/               # tratamento de erros e exceções
│   │   │   ├── infra/                    # repositories e acesso a dados
│   │   │   ├── service/                  # interfaces de serviço
│   │   │   ├── service/impl/             # implementações das regras de negócio
│   │   │   └── ui/                       # controllers / endpoints da API
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-local.properties
│   │
│   └── test/
│       ├── java/A3/project/noWaste/
│       │   ├── bdd/
│       │   │   ├── steps/
│       │   │   │   ├── AlertSteps.java
│       │   │   │   ├── BatchSteps.java
│       │   │   │   ├── CommonSteps.java
│       │   │   │   ├── Hooks.java
│       │   │   │   └── ProductSteps.java
│       │   │   │
│       │   │   ├── support/
│       │   │   │   ├── BDDContext.java
│       │   │   │   ├── CucumberSpringConfiguration.java
│       │   │   │   └── CucumberTest.java
│       │   │
│       │   ├── domain/
│       │   │   └── BatchTest.java
│       │   │
│       │   ├── service/
│       │   │   └── impl/
│       │   │       └── VerificationServiceTest.java
│       │   │
│       │   ├── ui/
│       │   │   ├── InventoryControllerTest.java
│       │   │   └── ProductControllerTest.java
│       │   │
│       │   └── NoWasteApplicationTests.java
│       │
│       └── resources/
│           └── features/
│               ├── alerts.feature
│               ├── batches.feature
│               └── products.feature
│
└── README.md                             # documentação do projeto
```

### Papel de cada camada

- **config**: configurações gerais, segurança e JWT
- **domain**: entidades principais do sistema
- **dto**: objetos de entrada e saída
- **exceptions**: tratamento de erros
- **infra**: repositories e acesso a dados
- **service / service/impl**: regras de negócio
- **ui**: controllers e endpoints REST

---

## ✅ Funcionalidades já presentes

O projeto já possui uma base funcional com:

- autenticação com JWT
- cadastro e login de usuários
- gerenciamento de inventários
- CRUD de produtos
- CRUD de lotes
- filtros e ordenações em consultas
- geração automática de código de lote
- cálculo automático de status dos lotes
- consultas específicas de alertas
- uso de DTOs
- tratamento padronizado de exceções

---

## 📘 Regras principais do sistema

### Produtos

- o cadastro aceita peso em **kg** ou **g**
- internamente, os valores são armazenados em **gramas**
- filtros de peso utilizam gramas como referência
- a API valida casos em que o peso mínimo é maior que o máximo

### Lotes

Cada lote recebe um código automático no padrão:

```text
LT-NOME_DO_PRODUTO-001
```

Exemplos:
- `LT-ARROZ-001`
- `LT-ARROZ-002`
- `LT-FEIJAO_PRETO-001`

A numeração é controlada por produto.

### Status de validade

O lote pode assumir os seguintes estados:

- `EXPIRED`
- `WARNING`
- `MONTH_WARNING`
- `OK`
- `UNKNOWN`

Esses valores são definidos conforme a proximidade da data de vencimento.

### Alertas disponíveis

A API já possui consultas para:

- lotes vencidos
- lotes que vencem nos próximos 7 dias
- lotes que vencem no mês atual

---

## 🌐 Rotas da API

### Autenticação

| Método | Rota | Função |
|---|---|---|
| POST | `/auth/login` | realiza o login e retorna o token |

### Usuários

| Método | Rota |
|---|---|
| GET | `/users` |
| POST | `/users` |
| PUT | `/users/{id}` |
| DELETE | `/users/{id}` |

### Inventários

| Método | Rota |
|---|---|
| GET | `/inventories` |
| GET | `/inventories/{id}` |
| POST | `/inventories` |
| PUT | `/inventories/{id}` |
| DELETE | `/inventories/{id}` |

Parâmetros suportados:
- `?name=mercado`
- `?sort=asc`
- `?sort=desc`

### Produtos

Base:
```text
/inventories/{inventoryId}/products
```

| Método | Rota |
|---|---|
| GET | `/products` |
| GET | `/inventories/{inventoryId}/products` |
| GET | `/inventories/{inventoryId}/products/{productId}` |
| POST | `/inventories/{inventoryId}/products` |
| PUT | `/inventories/{inventoryId}/products/{productId}` |
| DELETE | `/inventories/{inventoryId}/products/{productId}` |

Filtros:
- `?name=arroz`
- `?category=graos`
- `?brand=tio`
- `?minWeight=500&maxWeight=2000`
- `?sortWeight=asc`
- `?sortWeight=desc`

### Lotes

Base:
```text
/inventories/{inventoryId}/products/{productId}/batches
```

| Método | Rota |
|---|---|
| GET | `/inventories/{inventoryId}/products/{productId}/batches` |
| GET | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` |
| POST | `/inventories/{inventoryId}/products/{productId}/batches` |
| PUT | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` |
| DELETE | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` |

Filtros:
- `?code=LT`
- `?status=WARNING`
- `?expirationFrom=2026-04-01&expirationTo=2026-04-30`
- `?minQuantity=10&maxQuantity=50`
- `?sortExpiration=asc`
- `?sortExpiration=desc`

### Alertas

| Método | Rota              | Objetivo                       |
|---|-------------------|--------------------------------|
| GET | `/alerts/month`   | lotes que vencem no mês        |
| GET | `/alerts/week`    | lotes que vencem nos próximos 7 dias |
| GET | `/alerts/expired` | lotes já vencidos              |


---

## 🧪 Testes automatizados

O projeto conta com testes unitários, testes de controllers e testes BDD utilizando JUnit 5, Mockito e Cucumber.

Arquivos encontrados:
- BatchTest.java
- VerificationServiceTest.java
- InventoryControllerTest.java
- ProductControllerTest.java
- CucumberTest.java

### Executar testes

#### Windows

```bash
.\mvnw.cmd test
```

#### Linux/macOS

```bash
./mvnw test
```

### O que já está sendo validado

- cálculo de status dos lotes
- conversão de peso
- validação de filtros
- endpoints de inventário
- endpoints de produto
- validações do serviço de autenticação
- cenários BDD de negócio

---

## 🥒 Testes BDD

O projeto utiliza **Cucumber** para validar regras de negócio através de cenários BDD.

Estrutura principal:

```text
bdd/
├── steps/
├── support/
└── CucumberTest.java
```

Cenários cobertos:

- gerenciamento de produtos
- gerenciamento de lotes
- alertas de vencimento
- validações de regras de negócio

---

## Cobertura de testes com JaCoCo

Gerar relatório:

```bash
./mvnw clean verify
```

Windows:

```bash
.\mvnw.cmd clean verify
```

Caso esteja utilizando Windows, o relatório também pode ser aberto utilizando:

```bash
open-jacoco-report.bat
```

---

## 🧩 Trechos reais do projeto

### Cálculo de status no domínio `Batch`

```java
public String getStatus() {
    if (expirationDate == null) {
        return "UNKNOWN";
    }
    LocalDate today = LocalDate.now();
    long days = java.time.temporal.ChronoUnit.DAYS.between(today, expirationDate);

    if (days < 0) {
        return "EXPIRED";
    }
    if (days <= 7) {
        return "WARNING";
    }
    if (days <= 30) {
        return "MONTH_WARNING";
    }
    return "OK";
}
```

### Geração automática do código do lote

```java
private String generateBatchCode(Product product) {
    List<Batch> batches = repository.findByProductId(product.getId());

    int nextSequence = batches.stream()
            .map(Batch::getCode)
            .map(this::extractSequenceNumber)
            .max(Integer::compareTo)
            .orElse(0) + 1;

    String normalizedProductName = normalizeProductName(product.getName());

    return "LT-" + normalizedProductName + "-" + String.format("%03d", nextSequence);
}
```

---

## 🛠️ Problemas comuns

### Erro ao conectar no MySQL
Confira:
- se o banco está em execução
- se a porta informada está correta
- se o banco existe
- se usuário e senha estão certos

### Erro ao validar JWT
Revise o valor configurado em:

```properties
TOKEN_SECRET
```

### Erro no build Maven
Tente executar:

```bash
./mvnw clean install
```

ou no Windows:

```bash
.\mvnw.cmd clean install
```

### Problema com versão do Java
O projeto foi configurado com:

```xml
<java.version>21</java.version>
```
---

## 📌 Situação atual

- Sprint 0 concluída
- Sprint 1 concluída
- Sprint 2 concluída
- testes unitários implementados
- testes BDD implementados
- cobertura de testes com JaCoCo
- projeto em evolução para próximas entregas/Sprints

---

## 📄 Licença

Este projeto utiliza a licença **GPL-3.0**.

