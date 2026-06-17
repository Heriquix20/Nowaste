# 📦 noWaste — Controle Inteligente de Estoque

<p align="center">
  <strong>Sistema fullstack para gestão de inventários, produtos, lotes e alertas de vencimento.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Vers%C3%A3o%20Final-2E7D32?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Backend-Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Frontend-React-61DAFB?style=for-the-badge&logo=react&logoColor=black" />
  <img src="https://img.shields.io/badge/Banco-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
</p>

---

## 🧾 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [Problema e público-alvo](#-problema-e-público-alvo)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias utilizadas](#-tecnologias-utilizadas)
- [Arquitetura e estrutura de pastas](#-arquitetura-e-estrutura-de-pastas)
- [Rotas principais da API](#-rotas-principais-da-api)
- [Como executar o projeto](#-como-executar-o-projeto)
- [Como rodar os testes](#-como-rodar-os-testes)
- [Integrantes e papéis na Sprint](#-integrantes-e-papéis-na-sprint)
- [Licença](#-licença)

---

## 🌱 Sobre o projeto

O **noWaste** é uma aplicação fullstack desenvolvida para apoiar o controle de estoque com foco em **inventários, produtos, lotes e datas de validade**.  
O sistema permite que usuários cadastrem seus estoques, registrem produtos, adicionem lotes com quantidade e vencimento e acompanhem alertas automáticos.  
A solução foi construída com backend em **Spring Boot**, frontend em **React** e banco de dados em **MySQL**.  
O fluxo principal do sistema é organizado pela relação entre **usuário, inventário, produto, lote e alerta**, garantindo rastreabilidade dos itens cadastrados.  
O objetivo central é reduzir perdas causadas por produtos vencidos ou próximos do vencimento, oferecendo uma visão mais clara e prática do estoque.

Fluxo principal:

```text
Usuário → Inventário → Produto → Lote → Alertas de validade
```

---

## 🎯 Problema e público-alvo

Muitos pequenos negócios ainda controlam produtos perecíveis por cadernos, planilhas ou conferências manuais. Esse processo dificulta o acompanhamento de lotes, datas de validade e quantidade disponível, aumentando o risco de desperdício, perda financeira e falhas na reposição.

O **noWaste** atua nesse contexto oferecendo uma solução digital para organizar estoques e destacar automaticamente produtos vencidos ou próximos do vencimento.

| Problema observado | Solução proposta |
|---|---|
| Controle manual de validade | Alertas automáticos por data de vencimento |
| Falta de rastreio por lote | Cadastro de lotes vinculados a produtos |
| Dificuldade de localizar produtos | Filtros por nome, categoria, marca, peso, lote e validade |
| Perdas por vencimento | Identificação de lotes vencidos, semanais e mensais |
| Estoques desorganizados | Separação por inventários do usuário autenticado |

**Público-alvo:** supermercados, mercearias, pequenos comércios, estoques internos e negócios que trabalham com produtos perecíveis ou com data de validade.

---

## ✅ Funcionalidades

### Funcionalidades principais

- Cadastro e autenticação de usuários com token JWT.
- Criação, consulta, edição, exclusão, filtro e ordenação de inventários.
- Cadastro e listagem de produtos por inventário.
- Consulta global de produtos do usuário autenticado.
- Cadastro e gerenciamento de lotes por produto.
- Geração automática de código de lote no padrão `LT-NOME_DO_PRODUTO-001`.
- Conversão de peso de produtos de kg para gramas.
- Cálculo automático de status de validade do lote.
- Cálculo de dias restantes para vencimento.
- Cálculo de peso total do lote.
- Alertas para lotes vencidos, próximos do vencimento e vencendo no mês atual.
- Interface web com telas de apresentação, cadastro, login, inventários, produtos, lotes e alertas.

### Status de validade dos lotes

| Status | Significado |
|---|---|
| `EXPIRED` | Lote vencido |
| `WARNING` | Lote vence em até 7 dias |
| `MONTH_WARNING` | Lote vence entre 8 e 30 dias |
| `OK` | Lote com validade acima de 30 dias |
| `UNKNOWN` | Lote sem data de validade |

---

## 🧰 Tecnologias utilizadas

### Backend

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4.0.4 | Framework da API |
| Spring Web MVC | Criação dos endpoints REST |
| Spring Data JPA | Persistência e acesso a dados |
| Spring Security | Segurança da aplicação |
| JWT | Autenticação por token |
| ModelMapper | Conversão entre entidades e DTOs |
| Lombok | Redução de código repetitivo |
| MySQL | Banco de dados principal |
| H2 | Banco de apoio para testes |
| Maven | Gerenciamento de dependências e build |

### Frontend

| Tecnologia | Uso |
|---|---|
| React 19 | Construção da interface |
| Vite | Ambiente de desenvolvimento frontend |
| React Router DOM | Navegação entre telas |
| Axios | Comunicação HTTP com a API |
| CSS customizado | Estilização e identidade visual |

### Testes

| Ferramenta | Uso |
|---|---|
| JUnit 5 | Testes automatizados |
| Mockito | Simulação de dependências |
| MockMvc | Testes de controllers REST |
| Cucumber | Cenários BDD |
| JaCoCo | Apoio à análise de cobertura |

---

## 🏗️ Arquitetura e estrutura de pastas

```text
Nowaste/
├── noWaste/                         # Backend Spring Boot
│   ├── src/main/java/A3/project/noWaste/
│   │   ├── config/                  # Segurança, JWT, filtros e ModelMapper
│   │   ├── domain/                  # Entidades User, Inventory, Product e Batch
│   │   ├── dto/                     # Objetos de transferência de dados
│   │   ├── exceptions/              # Exceções e respostas de erro
│   │   ├── infra/                   # Repositories JPA
│   │   ├── service/                 # Contratos de serviços
│   │   ├── service/impl/            # Regras de negócio
│   │   └── ui/                      # Controllers REST
│   └── src/test/
│       ├── java/                    # Testes unitários, services e controllers
│       └── resources/features/      # Cenários BDD
│
├── nowaste-front/                   # Frontend React
│   ├── src/
│   │   ├── components/              # Componentes reutilizáveis
│   │   ├── pages/                   # Telas da aplicação
│   │   └── pages/services/api.js    # Configuração da API
│   └── package.json
│
└── estrutura_nowaste/docs/          # Documentação do projeto
    ├── requisitos/
    └── evidencias/
    └── testes/
    └── slides/
```

---

## 🌐 Rotas principais da API

### Autenticação

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Realiza login e retorna token JWT |

### Usuários

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/users` | Lista usuários |
| `POST` | `/users` | Cadastra usuário |
| `PUT` | `/users/{id}` | Atualiza usuário |
| `DELETE` | `/users/{id}` | Exclui usuário |

### Inventários

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/inventories` | Lista inventários do usuário autenticado |
| `GET` | `/inventories/{id}` | Consulta inventário por ID |
| `POST` | `/inventories` | Cria inventário |
| `PUT` | `/inventories/{id}` | Atualiza inventário |
| `DELETE` | `/inventories/{id}` | Exclui inventário |

Filtros:

```text
/inventories?name=mercado
/inventories?sort=asc
/inventories?sort=desc
```

### Produtos

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/products` | Lista produtos do usuário autenticado |
| `GET` | `/inventories/{inventoryId}/products` | Lista produtos de um inventário |
| `GET` | `/inventories/{inventoryId}/products/{productId}` | Consulta produto por ID |
| `POST` | `/inventories/{inventoryId}/products` | Cria produto |
| `PUT` | `/inventories/{inventoryId}/products/{productId}` | Atualiza produto |
| `DELETE` | `/inventories/{inventoryId}/products/{productId}` | Exclui produto |

Filtros:

```text
?name=arroz
?category=graos
?brand=tio
?minWeight=500&maxWeight=2000
?sortWeight=asc
?sortWeight=desc
```

### Lotes

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/inventories/{inventoryId}/products/{productId}/batches` | Lista lotes de um produto |
| `GET` | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` | Consulta lote por ID |
| `POST` | `/inventories/{inventoryId}/products/{productId}/batches` | Cria lote |
| `PUT` | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` | Atualiza lote |
| `DELETE` | `/inventories/{inventoryId}/products/{productId}/batches/{batchId}` | Exclui lote |

Filtros:

```text
?code=LT
?status=WARNING
?expirationFrom=2026-04-01&expirationTo=2026-04-30
?minQuantity=10&maxQuantity=50
?sortExpiration=asc
?sortExpiration=desc
```

### Alertas

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/alerts/month` | Lista lotes que vencem no mês atual |
| `GET` | `/alerts/week` | Lista lotes que vencem nos próximos 7 dias |
| `GET` | `/alerts/expired` | Lista lotes vencidos |

---

## 🚀 Como executar o projeto

### Pré-requisitos

| Ferramenta | Versão/observação |
|---|---|
| Java | JDK 21 |
| Maven | Wrapper incluído no backend |
| MySQL | Banco de dados local |
| Node.js | Recomendado para executar o frontend |
| npm | Gerenciador de pacotes |
| Git | Clonagem do repositório |

### 1. Clonar o repositório

```bash
git clone https://github.com/gabszinn/Nowaste.git
cd Nowaste
```

### 2. Configurar o backend

Acesse a pasta do backend:

```bash
cd noWaste
```

Crie um arquivo `.env` com as variáveis:

```properties
TOKEN_SECRET=seu_token_secret
MYSQL_PORT_KEY=3306
MYSQL_DATABASE_NAME=nome_do_banco
MYSQL_USERNAME_KEY=seu_usuario
MYSQL_PASSWORD_KEY=sua_senha
```

Execute o backend:

**Windows**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux/macOS**

```bash
./mvnw spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### 3. Executar o frontend

Em outro terminal, acesse a pasta do frontend:

```bash
cd nowaste-front
```

Instale as dependências:

```bash
npm install
```

Execute o projeto:

```bash
npm run dev
```

O frontend será iniciado pelo Vite, normalmente em:

```text
http://localhost:5173
```

A URL base da API está configurada em:

```javascript
baseURL: "http://localhost:8080"
```

---

## 🧪 Como rodar os testes

O backend possui testes automatizados para domínio, serviços, controllers e cenários BDD. A suíte cobre regras como cálculo de validade, filtros, conversão de peso, autenticação, proteção de usuário, endpoints e alertas.

### Executar todos os testes

**Windows**

```bash
cd noWaste
.\mvnw.cmd test
```

**Linux/macOS**

```bash
cd noWaste
./mvnw test
```

### Principais áreas testadas

| Área | Exemplos de cobertura |
|---|---|
| Domínio | Status de validade, dias restantes e peso total do lote |
| Services | Regras de produto, lote, inventário, usuário e alertas |
| Controllers | Endpoints REST e respostas HTTP |
| Segurança | Usuário autenticado, JWT e proteção de ações indevidas |
| BDD | Cenários de produtos, lotes e alertas |

### Evidência
<img width="1108" height="298" alt="image" src="https://github.com/user-attachments/assets/872974c3-29f9-45a1-9936-efa47b82cf98" />


---

## 👥 Integrantes e papéis na Sprint

| Nós | Nome / RA | Papel na Sprint |
|---|---|---|
| <img width="120" height="120" alt="image" src="https://github.com/user-attachments/assets/c8b44df7-89ba-4149-abfb-84cb307dea95" /> | **Gabriel Felipe Aguiar de Souza**<br>RA: 4231920576 | Product Owner |
| <img width="120" height="120" alt="image" src="https://github.com/user-attachments/assets/4d041ffa-0a39-4e3e-b0f4-2b7bb7ade237" /> | **Gabrielly dos Santos Ferreira**<br>RA: 42421704 | Frontend |
| <img width="120" height="120" alt="image" src="https://github.com/user-attachments/assets/daea1bf2-ec45-4187-b33a-7615b471464f" /> | **Isadora Rodrigues Pereira**<br>RA: 4231925937 | Frontend |
| <img width="120" height="120" alt="image" src="https://github.com/user-attachments/assets/516ebfd8-be07-4470-a7e4-93aad1f35fcb" /> | **Henrique Cezar Gomes Vieira**<br>RA: 4231924183 | Backend |
| <img width="120" height="120" alt="image" src="https://github.com/user-attachments/assets/a82a343a-9009-45ca-bd40-1218ffbebe6e" /> | **Weslley Ferreira de Oliveira**<br>RA: 4231924568 | Scrum Master |

---

## 📌 Status do projeto

| Área | Situação |
|---|---|
| Backend | Implementado |
| Frontend | Implementado |
| Autenticação | Implementada |
| Inventários, produtos e lotes | Implementados |
| Alertas de validade | Implementados |
| Testes unitários | Implementados |
| Testes BDD | Implementados |
| Documentação | Organizada para entrega final |

---

## 🛠️ Problemas comuns

### Erro de conexão com MySQL

Verifique se:

- o MySQL está ativo;
- o banco informado no `.env` existe;
- usuário, senha e porta estão corretos.

### Erro de autenticação JWT

Confirme se a variável abaixo foi definida:

```properties
TOKEN_SECRET=seu_token_secret
```

### Frontend não conecta à API

Verifique se o backend está rodando em:

```text
http://localhost:8080
```

E se o arquivo `api.js` aponta para a mesma URL.

### Problema com versão do Java

O projeto utiliza **Java 21**. Confirme a versão instalada com:

```bash
java -version
```

---

## 📄 Licença

Este projeto está sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.

