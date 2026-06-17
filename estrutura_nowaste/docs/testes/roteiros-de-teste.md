# 🧪📝 Roteiro de Teste — noWaste

---

## 📋 Roteiros de Testes

| ID | Prioridade | Funcionalidade | Objetivo | Pré-condição | Dados de teste | Passos | Resultado esperado | Evidência |
|---|---|---|---|---|---|---|---|---|
| RT-01 | 🔥 Alta | Cadastro de usuário | Cadastrar um usuário válido para acessar o sistema. | Aplicação iniciada e tela de cadastro disponível. | Nome: Ana Souza<br>E-mail: ana.nowaste@email.com<br>Senha: 123456 | 1) Acessar a tela de cadastro.<br>2) Informar nome, e-mail e senha.<br>3) Confirmar o cadastro. | Usuário cadastrado com sucesso e disponível para login. Caso o e-mail já exista, o sistema deve exibir mensagem de erro. | EVID-01 — Print da tela de cadastro + anotação do teste. |
| RT-02 | 🔥 Alta | Login de usuário | Validar se o usuário consegue acessar a área privada do sistema. | Usuário previamente cadastrado. | E-mail: ana.nowaste@email.com<br>Senha: 123456 | 1) Acessar a tela de login.<br>2) Informar e-mail e senha.<br>3) Confirmar login. | Sistema autentica o usuário e redireciona para a área de inventários. Credenciais inválidas devem gerar mensagem de erro. | EVID-02 — Print da tela de login + nota de sucesso. |
| RT-03 | 🔥 Alta | Cadastro de inventário | Criar um inventário/estoque para organizar produtos. | Usuário autenticado no sistema. | Nome do inventário: Estoque Mercado | 1) Acessar o painel de inventários.<br>2) Selecionar opção de criar inventário.<br>3) Informar o nome “Estoque Mercado”.<br>4) Confirmar criação. | O inventário deve ser criado e aparecer na listagem do usuário autenticado. | EVID-03 — Print do painel de inventários + formulário do observador. |
| RT-04 | ⚠️ Média | Listagem de inventários | Verificar se os inventários cadastrados aparecem corretamente. | Usuário autenticado e com pelo menos 1 inventário criado. | Inventário: Estoque Mercado | 1) Entrar no painel de inventários.<br>2) Verificar a listagem exibida.<br>3) Confirmar se o inventário criado aparece. | A lista deve exibir os inventários do usuário logado, sem mostrar dados de outros usuários. | EVID-04 — Print da listagem de inventários. |
| RT-05 | 🔥 Alta | Cadastro de produto | Cadastrar um produto dentro de um inventário. | Usuário autenticado e inventário criado. | Produto: Leite Integral<br>Categoria: Laticínios<br>Marca: Italac<br>Peso: 1<br>Unidade: kg | 1) Abrir o inventário “Estoque Mercado”.<br>2) Selecionar opção de cadastrar produto.<br>3) Preencher nome, categoria, marca, peso e unidade.<br>4) Confirmar cadastro. | Produto cadastrado e listado no inventário. Peso em kg deve ser tratado corretamente pelo sistema. | EVID-05 — Registro em formulário + print/listagem do produto. |
| RT-07 | 🔥 Alta | Cadastro de lote | Cadastrar lote de um produto com quantidade e validade. | Produto cadastrado dentro do inventário. | Produto: Leite Integral<br>Quantidade: 10<br>Validade: data próxima ao vencimento | 1) Abrir o produto “Leite Integral”.<br>2) Selecionar opção de lotes.<br>3) Cadastrar quantidade 10.<br>4) Informar data de validade próxima.<br>5) Confirmar cadastro. | Lote criado e vinculado ao produto. O sistema deve gerar código de lote automaticamente e calcular o status de validade. | EVID-07 — Nota do observador + print/modal de lote. |
| RT-08 | 🔥 Alta | Status de validade do lote | Verificar se o sistema classifica corretamente lotes vencidos ou próximos do vencimento. | Produto com lote cadastrado e data de validade preenchida. | Lote vencido<br>Lote próximo do vencimento<br>Lote dentro da validade | 1) Cadastrar ou consultar lotes com datas diferentes.<br>2) Observar o status exibido.<br>3) Comparar com a data de validade. | Sistema deve indicar corretamente situações como vencido, vence em breve, vence no mês ou dentro da validade. | EVID-08 — Print/nota de consulta dos lotes. |
| RT-09 | 🔥 Alta | Alertas de validade | Localizar alertas de lotes vencidos ou próximos do vencimento. | Usuário autenticado, inventário, produto e lote cadastrados. | Lote de Leite Integral vencendo no mês atual. | 1) Acessar área de alertas ou dashboard.<br>2) Consultar alertas de validade.<br>3) Verificar lotes vencidos e próximos do vencimento. | Sistema deve exibir alertas de validade de forma compreensível para o usuário. | EVID-09 — Print da tela de alertas + comentário do participante. |
| RT-10 | ⚠️ Média | Fluxo completo do sistema | Validar o caminho principal do noWaste do início ao fim. | Frontend e backend funcionando. | Usuário novo, inventário, produto e lote com validade próxima. | 1) Cadastrar usuário.<br>2) Fazer login.<br>3) Criar inventário.<br>4) Cadastrar produto.<br>5) Cadastrar lote.<br>6) Verificar alertas. | O usuário deve conseguir completar o fluxo **Usuário → Inventário → Produto → Lote → Alertas** sem bloqueios críticos. | EVID-10 — Checklist final do roteiro + notas do observador. |

---

## 📎 Evidências do roteiro

| Evidência | Descrição | Link/Registro |
|---|---|---|
| EVID-01 | Tela de cadastro do frontend | https://github.com/user-attachments/assets/1c0cfea1-6d83-4cb6-b8a3-e0baf94ab63d |
| EVID-02 | Tela de login do frontend | https://github.com/user-attachments/assets/811ad0bc-5ff5-4a45-8aa2-e048a5b06f16 |
| EVID-03 | Painel/listagem de inventários | https://github.com/user-attachments/assets/bbcc88c6-fa64-45e4-aeeb-a51eebe4a723 |
| EVID-04 | Consulta de inventários via API | https://github.com/user-attachments/assets/13791de3-5d5f-4938-aa7b-66f798bdd040 |
| EVID-05 | Registro manual do cadastro de produto | Formulário remoto + nota do observador |
| EVID-06 | Registro manual do filtro de produtos | Formulário remoto + nota do observador |
| EVID-07 | Registro manual do cadastro de lote | Formulário remoto + nota do observador |
| EVID-08 | Validação de status de lote | Notas do observador + resultado esperado do sistema |
| EVID-09 | Tela de alertas de validade | https://github.com/user-attachments/assets/490404fa-d6e3-4455-b07e-7b4f1f9abf5e |
| EVID-10 | Checklist do fluxo completo | Formulário final do teste de usabilidade |

---

## 📸 Prints de evidência

<img width="1038" height="290" alt="Print_01_RT-01_RT-02_RT-03_RT-04_RT-05_RT-06_RT-07_RT-08_RT-09_RT-10_backend_mvn_test_build_success" src="https://github.com/user-attachments/assets/877a6f48-4216-4676-93df-1607f7762b24" />

<img width="1588" height="436" alt="Print_02_RT-01_RT-02_RT-03_RT-04_RT-05_RT-06_RT-07_RT-08_RT-09_RT-10_testes_unitarios_IDE" src="https://github.com/user-attachments/assets/1da02795-96c6-4105-ad2f-bba19fd2937e" />

<img width="1037" height="565" alt="Print_03_RT-11_login_JWT_API" src="https://github.com/user-attachments/assets/c2413581-f86b-4336-a108-25bd345a6a57" />

<img width="915" height="924" alt="Print_04_RT-07_RT-11_RT-15_RT-17_consulta_inventarios_API" src="https://github.com/user-attachments/assets/13791de3-5d5f-4938-aa7b-66f798bdd040" />

<img width="983" height="899" alt="Print_05_RT-09_RT-16_RT-17_alertas_mes_API" src="https://github.com/user-attachments/assets/b645d5b7-7b66-4fc5-a1ef-f0982717cfee" />

<img width="973" height="931" alt="Print_06_RT-09_RT-16_RT-17_alertas_semana_API" src="https://github.com/user-attachments/assets/8f89f80a-f6a1-46da-ae2b-290da76dcf8f" />

<img width="1600" height="835" alt="Print_07_RT-12_tela_cadastro_frontend" src="https://github.com/user-attachments/assets/1c0cfea1-6d83-4cb6-b8a3-e0baf94ab63d" />

<img width="1600" height="835" alt="Print_08_RT-13_tela_login_frontend" src="https://github.com/user-attachments/assets/811ad0bc-5ff5-4a45-8aa2-e048a5b06f16" />

<img width="1600" height="835" alt="Print_09_RT-15_painel_inventarios_frontend" src="https://github.com/user-attachments/assets/bbcc88c6-fa64-45e4-aeeb-a51eebe4a723" />

<img width="1600" height="835" alt="Print_10_RT-16_alertas_validade_frontend" src="https://github.com/user-attachments/assets/490404fa-d6e3-4455-b07e-7b4f1f9abf5e" />

<img width="1600" height="835" alt="Print_11_GERAL_landing_page_inicio" src="https://github.com/user-attachments/assets/0c99a410-ad37-4d6e-b73d-4f0e5f14da64" />

<img width="1600" height="835" alt="Print_12_GERAL_landing_page_footer" src="https://github.com/user-attachments/assets/d3f2d98c-55df-45cb-bb28-e69675a72d95" />

<img width="1600" height="834" alt="image" src="https://github.com/user-attachments/assets/19332d95-2551-4028-9698-da1c6ac41469" />

<img width="1600" height="834" alt="image" src="https://github.com/user-attachments/assets/1a144647-4d8b-435c-94e7-4265259ebb86" />

<img width="1600" height="834" alt="image" src="https://github.com/user-attachments/assets/0483d27f-679c-47a6-bfe8-8b050053db6b" />

<img width="1600" height="834" alt="image" src="https://github.com/user-attachments/assets/7979c013-b658-4c8b-b334-f315fd4729e0" />

---
