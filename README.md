# 📦 Nowaste

## 🧾 Sobre o projeto

O **Nowaste** é um sistema em Java focado no controle de validade de produtos em estoque, utilizando gerenciamento por lotes.
A proposta é permitir que empresas acompanhem com mais precisão as datas de validade dos produtos, reduzindo desperdícios e prejuízos.

O sistema está sendo desenvolvido de forma incremental utilizando **Scrum**.
Atualmente, o projeto já contempla as entregas da **Sprint 0 e Sprint 1 (MVP)**.

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

## ⚙️ Funcionalidades

### ✔️ Sprint 0 (Concluída)

* Cadastro de produtos  
* Login de usuário  
* Cadastro de lotes  
* Associação entre produto e lote  
* Estrutura inicial do projeto  
* Organização em camadas (domain, service, infra, ui)  

---

### ✔️ Sprint 1 (MVP - Concluído/Em aperfeiçoamento)

* Listagem de produtos  
* Listagem de lotes  
* Filtros básicos  
* Status automático de validade  
* Estrutura inicial de testes com JUnit  

---

### 🚧 Próximas sprints

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

* Java (JDK 26)  
* Arquitetura em camadas  
* MySQL  
* JUnit  
* Maven  

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
     └── a3/project/noWaste/
         ├── config
         ├── domain
         ├── dto
         ├── exceptions
         ├── infra
         ├── service
         └── ui
```

---

## 🧪 Testes

```bash
# em desenvolvimento
```

---

## 💡 Exemplo de código do projeto

```java
public class Product {

    private String name;
    private double weight;

    public Product(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}
```

```java
public String getStatus(LocalDate expirationDate) {
    long days = ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);

    if (days < 0) return "EXPIRED";
    if (days <= 7) return "WARNING";
    if (days <= 30) return "MONTH_WARNING";
    return "OK";
}
```

---

## 👨‍💻 Integrantes e papéis (Sprint 0-1)

* Gabriel Felipe — Product Owner / Frontend  
* Isadora Rodrigues — Frontend  
* Wesley Carvalho — Scrum Master  
* Henrique Cezar — Backend  
* Gabrielly dos Santos — Frontend  

---

## 📌 Status do projeto

🚧 Em desenvolvimento  
✔️ Sprint 0 concluída  
🚧 Sprint 1 em andamento (MVP)

---

## 📄 Licença

GPL-3.0

