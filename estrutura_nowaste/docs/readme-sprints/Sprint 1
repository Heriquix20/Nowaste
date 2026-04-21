# 📦 noWaste

## 1. 📖 Visão Geral do Projeto

O **noWaste** é uma API backend em Java desenvolvida para apoiar o controle de estoque com foco em **validade de produtos por lote**, ajudando a reduzir desperdícios e melhorar o acompanhamento de vencimentos.

O projeto está sendo desenvolvido de forma incremental e, nesta etapa, está alinhado à **Sprint 1**, com foco em um **MVP funcional**, cobrindo o fluxo principal:

```
User → Inventory → Product → Batch
```

### ⚙️ Tecnologias utilizadas

- Java  
- Spring Boot  
- Maven  
- JPA / Hibernate  
- MySQL  
- JWT para autenticação  

---

## 2. 🎯 Escopo do MVP da Sprint 1

O MVP da Sprint 1 está concentrado principalmente nos módulos de:

- **Product**
- **Batch**
- **Alerts**

O objetivo desta sprint é entregar uma base funcional que permita:

- cadastrar e organizar produtos dentro de inventários  
- cadastrar lotes associados aos produtos  
- acompanhar validade e status dos lotes  
- consultar alertas relevantes para apoio à gestão do estoque  

A sprint também inclui os **testes iniciais** para começar a validar regras de negócio centrais da aplicação.

---

## 3. ✅ Funcionalidades Implementadas

Atualmente, o projeto já possui:

- autenticação com JWT  
- cadastro e login de usuários  
- CRUD de usuários  
- CRUD de inventários  
- CRUD de produtos  
- CRUD de lotes  
- filtros e ordenações para inventários, produtos e lotes  
- geração automática do código dos lotes  
- cálculo automático de status de lote  
- alertas para:
  - lotes vencidos  
  - lotes que vencem no mês atual  
- tratamento padronizado de erros com `ExceptionHandler`  
- uso de DTOs para entrada e saída de dados  
- arquitetura em camadas com:
  - Entity  
  - DTO  
  - Repository  
  - Service  
  - Controller  

---

## 4. 📌 Regras de Negócio

### 📦 Produtos

- O sistema aceita peso em **kg** ou **g** no cadastro e atualização de produtos.  
- Internamente, o peso é sempre **armazenado em gramas**.  
- Os filtros `minWeight` e `maxWeight` operam em **gramas**.  
- Quando `minWeight > maxWeight`, a API retorna erro de validação.  

---

### 📦 Lotes

O código do lote é gerado automaticamente no padrão:

```
LT-NOME_DO_PRODUTO-001
```

**Exemplos:**

- `LT-ARROZ-001`  
- `LT-ARROZ-002`  
- `LT-FEIJAO_PRETO-001`  

A sequência é controlada **por produto**.

---

### 📊 Status dos lotes

Os lotes possuem status calculado automaticamente com base na data de validade:

- `EXPIRED` → lote já vencido  
- `WARNING` → lote vence em até 7 dias  
- `MONTH_WARNING` → lote vence entre 8 e 30 dias  
- `OK` → lote vence em mais de 30 dias  

---

### 🚨 Alertas

O sistema possui consultas específicas para:

- lotes vencidos  
- lotes que vencem no mês atual  

---

## 5. 🧪 Testes

A Sprint 1 também contempla os **testes iniciais** do projeto.

A base de testes está estruturada com **JUnit 5**, com foco inicial em validação de regras de negócio relevantes, como:

- cálculo de status dos lotes  
- conversão e validação de peso dos produtos  
- geração automática do código dos lotes  

Nesta fase, os testes têm como objetivo validar os comportamentos centrais do MVP, sem ainda cobrir integralmente toda a aplicação.

---

## 6. ▶️ Como Rodar o Projeto

### 📋 Pré-requisitos

- Java 26  
- MySQL  
- Maven Wrapper do projeto  

---

### ⚙️ Configuração

O projeto utiliza variáveis externas para configuração local.  
Crie e ajuste o arquivo `.env` com os dados necessários:

```properties
TOKEN_SECRET=seu_token_secret
MYSQL_PORT_KEY=3306
MYSQL_DATABASE_NAME=nome_do_banco
MYSQL_USERNAME_KEY=seu_usuario
MYSQL_PASSWORD_KEY=sua_senha
```

---

### ▶️ Executando

**Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

O projeto utiliza o profile local configurado em `application.properties`.

---

## 7. 🔗 Resumo da API

### 🔐 Autenticação

#### Público

- `POST /users` → cadastra usuário  
- `POST /auth/login` → autentica e retorna token JWT  

#### Protegido

Todas as demais rotas exigem:

```http
Authorization: Bearer {token}
```

---

### 👤 Users

- `GET /users`  
- `PUT /users/{userId}`  
- `DELETE /users/{userId}`  

---

### 📁 Inventories

- `GET /inventories`  
- `GET /inventories/{inventoryId}`  
- `POST /inventories`  
- `PUT /inventories/{inventoryId}`  
- `DELETE /inventories/{inventoryId}`  

---

### 🏷️ Products

- `GET /inventories/{inventoryId}/products`  
- `GET /inventories/{inventoryId}/products/{productId}`  
- `POST /inventories/{inventoryId}/products`  
- `PUT /inventories/{inventoryId}/products/{productId}`  
- `DELETE /inventories/{inventoryId}/products/{productId}`  

---

### 📦 Batches

- `GET /inventories/{inventoryId}/products/{productId}/batches`  
- `GET /inventories/{inventoryId}/products/{productId}/batches/{batchId}`  
- `POST /inventories/{inventoryId}/products/{productId}/batches`  
- `PUT /inventories/{inventoryId}/products/{productId}/batches/{batchId}`  
- `DELETE /inventories/{inventoryId}/products/{productId}/batches/{batchId}`  

---

### 🚨 Alerts

- `GET /alerts/month`  
- `GET /alerts/expired`  

---

## 8. 📊 Alinhamento com a Sprint 1

O projeto atende ao objetivo da **Sprint 1** por já entregar um **MVP funcional**.
