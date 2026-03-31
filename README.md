# 📦 Nowaste

## 🧾 Sobre o projeto

O Nowaste é um sistema em Java focado no controle de validade de produtos em estoque, utilizando gerenciamento por lotes.
A proposta é permitir que empresas acompanhem com mais precisão as datas de validade dos produtos, reduzindo desperdícios e prejuízos.

O sistema está sendo desenvolvido de forma incremental utilizando Scrum, evoluindo a cada sprint com novas funcionalidades e melhorias.

---

## 🎯 Problema

Empresas que trabalham com estoque enfrentam dificuldades como:

* controle de validade por lote
* identificação de produtos próximos do vencimento
* desperdício de produtos

---

## 👥 Público-alvo

* supermercados
* mercearias
* qualquer negócio com controle de estoque por validade

---

## ⚙️ Funcionalidades a serem implementadas:

### ✔️ Sprint 0 (Atual)

* Cadastro de produtos
* Aba de Login
* Cadastro de lotes
* Associação entre produto e lote
* Estrutura inicial do projeto
* Organização em camadas (domain, service, repository, ui)

---

### 🚧 Próximas sprints

#### Sprint 1 (MVP)

* Listagem de produtos
* Listagem de lotes
* Primeiros testes automatizados

#### Sprint 2

* Identificação de produtos próximos do vencimento
* Produtos que vencem no mês
* Refinamento das regras de negócio

#### Sprint 3

* Testes completos
* Melhorias de usabilidade
* Documentação final
* Apresentação

---

## 🛠️ Tecnologias utilizadas

* Java
* Estrutura em camadas (Domain, Service, Repository, UI)
* JUnit (planejado)

---

## ▶️ Como executar

```bash
git clone https://github.com/gabszinn/Nowaste.git
```

Abrir na IDE (IntelliJ ou Eclipse) e executar a classe principal.

---

## 🧱 Estrutura do projeto

```bash
src/
 └── main/java/
     ├── domain
     ├── service
     ├── repository
     └── ui
```

---

## 🧪 Como rodar os testes

```bash
# ainda em desenvolvimento
```

Testes serão implementados a partir da Sprint 1 utilizando JUnit.

---

## 👨‍💻 Integrantes e papéis (Sprint 0)

* Gabriel Felipe — Product Owner/backend
* Isadora Rodrigues — backend
* Wesley Carvalho — backend
* Henrique Cezar — backend

---

## 📌 Status do projeto

🚧 Em desenvolvimento (Sprint 0 concluída)

---

## 📄 Licença

GPL-3.0
