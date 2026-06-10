# 🧪✅ Plano de Teste — noWaste

## 📋 Convenções e status

| Item | Padrão ✅ |
|---|---|
| 🏷️ ID do teste | **RT-XX** para roteiro manual, **UT-XX** para teste unitário e **BDD-XX** para cenário BDD |
| 📌 Status | 🟡 Planejado • 🔵 Em execução • 🟢 Passou • 🔴 Falhou • ⚫ Bloqueado |
| ⭐ Prioridade | 🔥 Alta • ⚠️ Média • 🟦 Baixa |
| 📎 Evidência | Print / log / vídeo / link do PR/Issue / execução de teste |

---

## 🆔📖 Identificação e contexto

| Campo | Preencher ✍️ |
|---|---|
| 🧩 Nome do projeto | **noWaste** |
| 📝 Objetivo do sistema (resumo) | Sistema web/API para controle de estoque por inventário, produto, lote e validade, permitindo acompanhar vencimentos e reduzir perdas por produtos expirados. |
| 🎯 Público-alvo | Pequenos comércios, mercados, mercearias, estoques internos, empresas com produtos perecíveis ou produtos com data de validade. |
| 💻 Plataforma/Tipo | **Aplicação web + API REST** |
| 🔗 Repositório | `https://github.com/gabszinn/Nowaste.git` |
| 👥 Time/Grupo | Gabriel Felipe, Isadora Rodrigues, Wesley Carvalho, Henrique Cezar e Gabrielly dos Santos |

---

## 🎯🧪 Objetivo do teste

| Item | Descrição 🗒️ |
|---|---|
| ✅ Objetivo geral | Validar se o noWaste atende ao fluxo principal do sistema: cadastro de usuário, autenticação, criação de inventários, produtos e lotes, cálculo de validade, filtros, alertas e exibição das telas do frontend. |
| 📊 Metas de cobertura | Garantir cobertura das regras de negócio principais no backend com testes unitários e validar os fluxos críticos da Sprint 2 por meio de testes manuais, prints, logs e evidências de API/frontend. |

---

## 📦📌 Escopo

| Categoria | ✅ Em escopo | 🚫 Fora de escopo |
|---|---|---|
| 🧩 Funcionalidades | Cadastro/login, inventários, produtos, lotes, alertas, filtros, ordenações, telas do frontend e fluxo principal do sistema. | Funcionalidades futuras ainda não implementadas, como relatórios avançados, dashboard analítico completo e permissões por múltiplos perfis. |
| 🧠 Regras de negócio | Status de validade, dias restantes, peso total do lote, conversão kg/g, geração automática de código de lote, validação de filtros e proteção por usuário autenticado. | Regras financeiras, controle de compras, vendas, fornecedores e emissão de notas. |
| 🔌 Integrações | Integração frontend/backend, autenticação JWT e consumo de endpoints REST. | Integrações externas, como ERP, emissão fiscal, serviços de e-mail, WhatsApp ou notificações externas. |
| 🗃️ Dados | Usuários, inventários, produtos, lotes, datas de vencimento, quantidades, pesos, categorias e marcas. | Dados reais de clientes, dados sensíveis de produção e carga massiva de banco. |
| 🧑‍💻 Não-funcionais | Usabilidade básica, organização visual das telas, mensagens de erro, navegação e responsividade inicial. | Teste de carga, stress test, segurança avançada, acessibilidade completa e auditoria de performance em produção. |

---

## 🧰🖥️ Ambiente e ferramentas

| Item | Especificação ⚙️ |
|---|---|
| 🖥️ SO | Windows / Linux / macOS em ambiente local de desenvolvimento |
| ☕ Linguagem/Runtime | Java 26 no backend; JavaScript/TypeScript no frontend |
| 🧑‍💻 IDE | IntelliJ IDEA, Eclipse ou VS Code |
| 🧱 Build | Maven no backend; gerenciador de pacotes do frontend conforme projeto enviado |
| ✅ Framework de testes unitários | JUnit 5 e Mockito |
| 🥒 BDD | Cenários documentados em roteiro/issue, sem obrigatoriedade de automação nesta Sprint |
| 🤖 CI | Não configurado/informado para esta entrega |
| 🗄️ Banco/Dados | MySQL em ambiente local; H2 disponível para apoio em desenvolvimento/testes |

---

## 🧪🧱 Estratégia de testes por tipo

| Tipo de teste | 🎯 Objetivo | 📌 Escopo | 🛠️ Ferramenta | 👤 Responsável | 📎 Saída/Evidência |
|---|---|---|---|---|---|
| ✅ Unitário | Validar regras de negócio isoladas do backend. | Entidades e services de lote, produto, inventário, usuário, alertas e autenticação. | JUnit 5, Mockito e Maven (`mvn test`). | Backend | Print/log da execução dos testes e classes de teste. |
| 🌐 Sistema/End-to-End | Validar o fluxo completo do usuário no sistema. | Cadastro → login → inventário → produto → lote → alertas. | Navegador, Postman/Insomnia e execução local da aplicação. | Time de desenvolvimento/testes | Prints sequenciais ou vídeo curto do fluxo. |
| 🥒 BDD | Descrever o comportamento esperado em linguagem próxima do usuário. | Fluxos principais e regras críticas da Sprint 2. | Issues, documentação ou arquivo `.feature` caso seja criado. | Time Scrum | Cenários documentados no Kanban ou no repositório. |
| 🧑‍💻 Usabilidade | Verificar se as telas estão compreensíveis e utilizáveis. | Landing page, cadastro, login, inventários, produtos, lotes e alertas. | Navegador e checklist manual. | Frontend | Prints das telas e observações de uso. |
| 🔌 API/Integração | Validar endpoints reais e respostas do backend. | Login JWT, inventários, produtos, lotes, alertas e erros esperados. | Postman/Insomnia. | Backend/Fullstack | Prints das requisições e respostas HTTP. |

---

## 🧷🧭 Rastreabilidade — Requisitos x Testes

| ID Req | Requisito/Funcionalidade | ⭐ Prioridade | 🔗 Fonte (Issue/PR) | 🧪 IDs de testes | 📌 Status |
|---|---|---|---|---|---|
| RF-01 | Cadastro de usuário | 🔥 Alta | Issue/Backend/Frontend | UT-08, RT-08, RT-12, BDD-01 | 🟢 Executado parcialmente |
| RF-02 | Login com JWT | 🔥 Alta | Issue/Backend/API | RT-11, RT-13, BDD-02 | 🔵 Em execução |
| RF-03 | Proteção por usuário autenticado | 🔥 Alta | Backend/Security | UT-10, RT-14 | 🔵 Em execução |
| RF-04 | CRUD de inventários | 🔥 Alta | Issue Inventário | UT-07, RT-07, RT-15, BDD-03 | 🟢 Executado parcialmente |
| RF-05 | Cadastro e filtro de produtos | 🔥 Alta | Issue Produto | UT-05, UT-06, RT-05, RT-06 | 🟢 Executado |
| RF-06 | Cadastro e controle de lotes | 🔥 Alta | Issue Lote | UT-01, UT-02, UT-03, UT-04, RT-01 a RT-04 | 🟢 Executado |
| RF-07 | Cálculo automático de validade | 🔥 Alta | Backend/Batch | UT-01, RT-01 | 🟢 Executado |
| RF-08 | Alertas de vencimento | 🔥 Alta | Issue Alertas | UT-09, RT-09, RT-16 | 🟢 Executado parcialmente |
| RF-09 | Filtros e ordenações | ⚠️ Média | Issues Produto/Lote/Inventário | UT-04, UT-06, UT-07, RT-04, RT-06 | 🟢 Executado |
| RF-10 | Telas do frontend | 🔥 Alta | Issues Frontend | RT-12, RT-13, RT-15, RT-16, RT-18 | 🟢 Executado  |
| RF-11 | Fluxo completo ponta a ponta | 🔥 Alta | Sprint 2 | RT-18, BDD-04 | 🟡 Planejado |
| RF-12 | Documentação da Sprint 2 | ⚠️ Média | README/Roteiros | RT-DOC-01 | 🟢 Executado |

---

## 🧾🧪 Casos de teste planejados — resumo

| ID | 🧪 Tipo | 🏷️ Título | 🔐 Pré-condição | 📥 Entrada | ✅ Resultado esperado | ⭐ Prioridade | 🤖 Automatizado? |
|---|---|---|---|---|---|---|---|
| UT-01 | ✅ Unitário | Status de validade do lote | Classe `Batch` disponível | Datas vencidas, próximas e nulas | Retornar `EXPIRED`, `WARNING`, `MONTH_WARNING`, `OK` ou `UNKNOWN` | 🔥 Alta | Sim |
| UT-02 | ✅ Unitário | Dias restantes e peso total do lote | Produto relacionado ao lote | Peso 500g, quantidade 4, validade em 4 dias | Peso total 2000g e 4 dias restantes | 🔥 Alta | Sim |
| UT-03 | ✅ Unitário | Código automático do lote | Produto cadastrado | Produto Arroz e Feijao Preto | Gerar códigos sequenciais no padrão `LT-PRODUTO-001` | 🔥 Alta | Sim |
| UT-04 | ✅ Unitário | Filtros e ordenação de lotes | Lotes cadastrados | Código, status, validade, quantidade | Retornar apenas lotes compatíveis | 🔥 Alta | Sim |
| UT-05 | ✅ Unitário | Cadastro de produto com peso | Inventário existente | Produto com 1kg e 500g | Armazenar peso internamente em gramas | 🔥 Alta | Sim |
| UT-06 | ✅ Unitário | Filtros e validações de produto | Produtos cadastrados | Nome, categoria, marca, peso mínimo e máximo | Filtrar corretamente e validar erro de peso inválido | 🔥 Alta | Sim |
| UT-07 | ✅ Unitário | Gerenciamento de inventários | Usuário autenticado | Criar, listar, editar e excluir inventário | Permitir CRUD e bloquear duplicidade | 🔥 Alta | Sim |
| UT-08 | ✅ Unitário | Cadastro e proteção de usuário | Repositório de usuários | Usuário novo, duplicado e outro usuário | Criptografar senha e bloquear ações indevidas | 🔥 Alta | Sim |
| UT-09 | ✅ Unitário | Alertas de validade | Lotes com datas diferentes | Lote vencido e lote do mês | Retornar alertas corretos por usuário | 🔥 Alta | Sim |
| UT-10 | ✅ Unitário | Usuário autenticado | Contexto JWT configurado | Usuário existente, inexistente e sem autenticação | Identificar usuário ou lançar exceção | 🔥 Alta | Sim |
| RT-01 | 📝 Manual/API | Login JWT via API | Backend rodando e usuário criado | E-mail e senha válidos | API retorna token JWT | 🔥 Alta | Não |
| RT-02 | 📝 Manual/Frontend | Cadastro de usuário pelo frontend | Backend e frontend rodando | Nome, e-mail e senha | Usuário cadastrado ou erro exibido | 🔥 Alta | Não |
| RT-03 | 📝 Manual/Frontend | Login pelo frontend | Usuário cadastrado | Credenciais válidas/inválidas | Login realizado e usuário redirecionado | 🔥 Alta | Não |
| RT-04 | 📝 Manual/Frontend | Acesso protegido | Token válido ou ausente | Abrir painel privado | Bloquear sem token e liberar com token válido | 🔥 Alta | Não |
| RT-05 | 📝 Manual/Frontend | Listagem de inventários | Usuário autenticado | Inventários cadastrados | Tela exibe inventários do usuário | 🔥 Alta | Não |
| RT-06 | 📝 Manual/Frontend | Dashboard de alertas | Lotes cadastrados | Lotes vencidos/próximos | Tela exibe alertas corretamente | ⚠️ Média | Não |
| RT-07 | 📝 Manual/API | Padronização de respostas | Endpoints disponíveis | Dados válidos, inválidos e sem token | Respostas com padrão previsível | ⚠️ Média | Não |
| RT-08 | 📝 Manual/E2E | Fluxo completo | Frontend/backend rodando | Usuário, inventário, produto e lote | Concluir fluxo até alertas | 🔥 Alta | Não |
| BDD-01 | 🥒 BDD | Cadastro de usuário | Tela de cadastro disponível | Dados válidos | Usuário cadastrado com sucesso | 🔥 Alta | Não |
| BDD-02 | 🥒 BDD | Login de usuário | Usuário existente | Credenciais válidas | Usuário acessa área privada | 🔥 Alta | Não |
| BDD-03 | 🥒 BDD | Cadastro de lote com validade | Produto existente | Data de vencimento próxima | Sistema classifica status corretamente | 🔥 Alta | Não |
| BDD-04 | 🥒 BDD | Alerta de vencimento | Lote vencendo no mês | Abrir dashboard | Sistema exibe alerta de validade | 🔥 Alta | Não |

---

## 🗃️🧪 Dados de teste

| ID | 🧺 Conjunto | 📝 Descrição | 🧪 Como criar | 📍 Onde armazenar | 💡 Observações |
|---|---|---|---|---|---|
| DT-01 | Usuário válido | Usuário para login e testes de autenticação | Criar via `POST /users` ou tela de cadastro | Banco MySQL local | Usar e-mail único para evitar duplicidade |
| DT-02 | Usuário duplicado | Usuário com e-mail já cadastrado | Repetir cadastro com mesmo e-mail | Banco MySQL local | Deve retornar erro de validação |
| DT-03 | Inventário | Inventário de teste do usuário | Criar via frontend ou `POST /inventories` | Banco MySQL local | Exemplo: `Estoque Mercado` |
| DT-04 | Produto em gramas | Produto com peso informado em gramas | Criar produto com `500g` | Banco MySQL local | Deve manter 500g |
| DT-05 | Produto em kg | Produto com peso informado em kg | Criar produto com `1kg` | Banco MySQL local | Deve converter para 1000g |
| DT-06 | Lote vencido | Lote com validade anterior à data atual | Criar lote com data passada | Banco MySQL local | Esperado: `EXPIRED` |
| DT-07 | Lote próximo | Lote com validade em até 7 dias | Criar lote com validade próxima | Banco MySQL local | Esperado: `WARNING` |
| DT-08 | Lote do mês | Lote com validade entre 8 e 30 dias | Criar lote com validade dentro do mês | Banco MySQL local | Esperado: `MONTH_WARNING` |
| DT-09 | Lote normal | Lote com validade acima de 30 dias | Criar lote com validade futura | Banco MySQL local | Esperado: `OK` |
| DT-10 | Lote sem validade | Lote sem data de validade | Criar lote com validade nula, se permitido | Banco MySQL local/teste unitário | Esperado: `UNKNOWN` |

---
