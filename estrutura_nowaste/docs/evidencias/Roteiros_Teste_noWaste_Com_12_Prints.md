# 🧾 Roteiros de Teste — noWaste

## 📋 Convenções e status

| Item | Padrão ✅ |
| --- | --- |
| 🏷️ ID do teste | **RT-XX** |
| 📌 Status | 🟡 Planejado • 🔵 Em execução • 🟢 Passou • 🔴 Falhou • ⚫ Bloqueado |
| ⭐ Prioridade | 🔥 Alta • ⚠️ Média • 🟦 Baixa |
| 🧾 Evidência | Existe • Não existe |

---

## 🧪 Tabela de roteiros de teste

| 🏷️ ID | ⭐ Prioridade | 🧩 Funcionalidade | 🎯 Objetivo | 🔐 Pré-condição | 🧪 Dados de teste | 🪜 Passos | ✅ Resultado esperado | 📌 Status | 🧾 Evidência |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| RT-01 | 🔥 Alta | Status de validade do lote | Validar se o sistema calcula corretamente o status do lote conforme a data de vencimento. | Backend configurado e entidade `Batch` disponível. | Data vencida, data em até 7 dias, data entre 8 e 30 dias, data acima de 30 dias e data nula. | 1) Criar lote com validade no passado.<br>2) Criar lote com validade em até 7 dias.<br>3) Criar lote com validade entre 8 e 30 dias.<br>4) Criar lote com validade acima de 30 dias.<br>5) Criar lote sem data de validade. | O sistema deve retornar `EXPIRED`, `WARNING`, `MONTH_WARNING`, `OK` e `UNKNOWN` conforme cada cenário. | 🟢 Passou | Existe |
| RT-02 | 🔥 Alta | Cálculo de dias restantes e peso total do lote | Validar cálculo de dias até vencimento e cálculo do peso total do lote. | Backend configurado e entidade `Batch` relacionada a `Product`. | Produto com peso de 500g e lote com quantidade 4. Lote com validade em 4 dias. | 1) Criar produto com peso de 500g.<br>2) Criar lote com quantidade 4.<br>3) Consultar peso total do lote.<br>4) Criar lote com validade em 4 dias.<br>5) Consultar dias restantes. | O peso total deve ser 2000g. Os dias restantes devem ser 4. Caso não exista produto ou validade, o retorno deve ser tratado corretamente. | 🟢 Passou | Existe |
| RT-03 | 🔥 Alta | Geração automática de código de lote | Validar se o código do lote é gerado automaticamente no padrão definido pelo sistema. | Produto cadastrado dentro de um inventário pertencente ao usuário autenticado. | Produto `Arroz`, produto `Feijao Preto` e lotes existentes. | 1) Criar primeiro lote do produto Arroz.<br>2) Criar segundo lote do mesmo produto.<br>3) Criar lote para outro produto.<br>4) Verificar código gerado. | O sistema deve gerar `LT-ARROZ-001`, depois `LT-ARROZ-002` para o mesmo produto e iniciar nova sequência para outro produto, como `LT-FEIJAO_PRETO-001`. | 🟢 Passou | Existe |
| RT-04 | 🔥 Alta | Filtros e ordenação de lotes | Validar se a listagem de lotes permite filtros por código, status, validade, quantidade e ordenação por vencimento. | Usuário autenticado, inventário existente, produto existente e lotes cadastrados. | Lotes com códigos diferentes, status diferentes, quantidades diferentes e datas de validade diferentes. | 1) Listar lotes de um produto.<br>2) Aplicar filtro por código.<br>3) Aplicar filtro por status.<br>4) Aplicar filtro por intervalo de validade.<br>5) Aplicar filtro por quantidade mínima e máxima.<br>6) Aplicar ordenação por validade. | A API deve retornar apenas os lotes compatíveis com os filtros informados e deve ordenar corretamente quando solicitado. | 🟢 Passou | Existe |
| RT-05 | 🔥 Alta | Cadastro de produto com peso | Validar se o sistema aceita peso em kg ou g e armazena internamente em gramas. | Usuário autenticado e inventário existente. | Produto com peso `1kg`, produto com peso `500g`. | 1) Criar produto informando peso em kg.<br>2) Verificar conversão para gramas.<br>3) Criar produto informando peso em g.<br>4) Verificar se o valor foi mantido em gramas. | Produto com `1kg` deve ser armazenado como `1000g`. Produto com `500g` deve ser armazenado como `500g`. | 🟢 Passou | Existe |
| RT-06 | 🔥 Alta | Filtros e validações de produto | Validar filtros de produtos e regra de peso mínimo maior que peso máximo. | Usuário autenticado, inventário existente e produtos cadastrados. | Produtos `Arroz Branco`, `Feijao`, `Macarrao`, categorias, marcas e pesos diferentes. | 1) Listar produtos do inventário.<br>2) Filtrar por nome.<br>3) Filtrar por categoria.<br>4) Filtrar por marca.<br>5) Filtrar por peso mínimo e máximo.<br>6) Ordenar por peso.<br>7) Testar peso mínimo maior que peso máximo. | A API deve retornar apenas os produtos filtrados. Quando peso mínimo for maior que peso máximo, deve retornar erro de validação. | 🟢 Passou | Existe |
| RT-07 | 🔥 Alta | Gerenciamento de inventários | Validar criação, consulta, atualização, exclusão, filtro e ordenação de inventários do usuário. | Usuário autenticado. | Inventários com nomes diferentes, inventário duplicado e inventário inexistente. | 1) Criar inventário.<br>2) Consultar inventário por ID.<br>3) Filtrar inventário por nome.<br>4) Ordenar inventários.<br>5) Atualizar inventário.<br>6) Excluir inventário.<br>7) Tentar criar inventário duplicado. | O sistema deve permitir CRUD de inventário para o usuário autenticado, bloquear duplicidade de nome e retornar erro quando o inventário não existir. | 🟢 Passou | Existe |
| RT-08 | 🔥 Alta | Cadastro e proteção de usuário | Validar criação de usuário, criptografia de senha, bloqueio de e-mail duplicado e restrição para alterar/excluir outro usuário. | Backend configurado e repositório de usuários disponível. | Usuário `Ana`, e-mail `ana@email.com`, senha `123456`; usuário duplicado; usuário autenticado diferente do alvo. | 1) Criar usuário com e-mail válido.<br>2) Verificar senha criptografada.<br>3) Tentar criar usuário com e-mail duplicado.<br>4) Tentar atualizar outro usuário.<br>5) Tentar excluir outro usuário. | O sistema deve criptografar a senha, impedir e-mail duplicado e bloquear ações sobre usuário diferente do autenticado. | 🟢 Passou | Existe |
| RT-09 | 🔥 Alta | Alertas de validade | Validar endpoints de alertas para lotes vencidos e lotes que vencem no mês atual. | Usuário autenticado, inventários, produtos e lotes cadastrados com diferentes datas de validade. | Lote vencido, lote que vence no mês atual e lote fora do intervalo. | 1) Consultar alertas de vencimento no mês.<br>2) Consultar alertas de lotes vencidos.<br>3) Verificar ordenação por validade.<br>4) Verificar se apenas dados do usuário autenticado são retornados. | `/alerts/month` deve retornar lotes que vencem no mês. `/alerts/expired` deve retornar lotes vencidos. Ambos devem respeitar o usuário autenticado. | 🟢 Passou | Existe |
| RT-10 | 🔥 Alta | Verificação do usuário autenticado | Validar se o sistema identifica corretamente o usuário autenticado pelo contexto de segurança. | Contexto de segurança configurado com dados JWT. | Usuário autenticado com ID 1; usuário inexistente; ausência de autenticação. | 1) Simular usuário autenticado.<br>2) Buscar ID do usuário autenticado.<br>3) Buscar usuário no repositório.<br>4) Simular ausência de autenticação.<br>5) Simular usuário autenticado inexistente. | O sistema deve retornar o ID correto, buscar o usuário autenticado e lançar exceção quando não houver usuário válido. | 🟢 Passou | Existe |
| RT-11 | 🔥 Alta | Login com JWT via API | Validar se o endpoint real de login retorna token JWT para credenciais válidas. | Backend rodando, banco configurado e usuário cadastrado. | E-mail e senha de usuário existente. | 1) Criar usuário via `POST /users`.<br>2) Enviar e-mail e senha para `POST /auth/login`.<br>3) Verificar resposta da API.<br>4) Copiar token retornado.<br>5) Usar token em rota protegida. | A API deve retornar token JWT. O token deve permitir acesso a rotas protegidas. Credenciais inválidas devem retornar erro. | 🟡 Planejado | Existe |
| RT-12 | 🔥 Alta | Cadastro de usuário pelo frontend | Validar se a tela de cadastro envia dados corretamente para o backend. | Backend rodando, frontend rodando e API configurada em `http://localhost:8080`. | Nome, e-mail e senha válidos. E-mail duplicado para cenário de erro. | 1) Acessar tela de cadastro.<br>2) Preencher nome, e-mail e senha.<br>3) Enviar formulário.<br>4) Verificar resposta de sucesso.<br>5) Tentar cadastrar e-mail duplicado. | O usuário deve ser cadastrado com sucesso. Em caso de e-mail duplicado, o frontend deve exibir mensagem de erro. | 🟡 Planejado | Existe |
| RT-13 | 🔥 Alta | Login real pelo frontend | Validar se a tela de login usa `POST /auth/login`, salva o JWT e redireciona para a área privada. | Backend e frontend rodando. Usuário previamente cadastrado. Login ajustado para usar o endpoint real. | E-mail e senha válidos; e-mail/senha inválidos. | 1) Acessar tela de login.<br>2) Informar credenciais válidas.<br>3) Enviar formulário.<br>4) Verificar armazenamento do token no navegador.<br>5) Confirmar redirecionamento para `/inventory`.<br>6) Testar credenciais inválidas. | O frontend deve salvar o token JWT e redirecionar o usuário autenticado. Credenciais inválidas devem exibir erro. | 🟡 Planejado | Existe |
| RT-14 | 🔥 Alta | Acesso protegido ao painel de inventários | Validar se a rota de inventário só pode ser acessada por usuário autenticado. | Frontend com proteção de rota implementada e token JWT disponível após login. | Token válido, ausência de token e token inválido. | 1) Acessar `/inventory` sem token.<br>2) Verificar redirecionamento para login.<br>3) Fazer login.<br>4) Acessar `/inventory` com token válido.<br>5) Remover token e tentar acessar novamente. | O acesso sem token deve ser bloqueado. Com token válido, o painel deve abrir normalmente. | 🟡 Planejado | Não existe |
| RT-15 | 🔥 Alta | Listagem de inventários no frontend | Validar se o painel de inventários consome `GET /inventories` usando JWT. | Backend rodando, usuário autenticado e inventários cadastrados. | Usuário com inventários; usuário sem inventários. | 1) Fazer login.<br>2) Acessar painel de inventários.<br>3) Verificar chamada para `GET /inventories`.<br>4) Conferir se o token foi enviado.<br>5) Testar cenário sem inventários. | O frontend deve listar inventários do usuário autenticado, mostrar carregamento, erro ou estado vazio quando necessário. | 🟡 Planejado | Existe |
| RT-16 | ⚠️ Média | Dashboard de alertas no frontend | Validar se o frontend exibe lotes vencidos e lotes que vencem no mês. | Backend com alertas funcionando, frontend autenticado e lotes de teste cadastrados. | Lote vencido, lote vencendo no mês e usuário sem alertas. | 1) Fazer login.<br>2) Acessar dashboard/painel.<br>3) Consumir `/alerts/expired`.<br>4) Consumir `/alerts/month`.<br>5) Verificar cards/lista de alertas.<br>6) Testar cenário sem alertas. | O frontend deve exibir alertas de forma clara, destacando lotes vencidos e próximos do vencimento. | 🟡 Planejado | Existe |
| RT-17 | ⚠️ Média | Padronização das respostas da API | Validar se respostas de sucesso e erro seguem um padrão consistente para o frontend. | Backend rodando e endpoints principais disponíveis. | Requisições válidas, dados inválidos, recurso inexistente e acesso não autorizado. | 1) Testar criação com sucesso.<br>2) Testar validação inválida.<br>3) Testar busca por recurso inexistente.<br>4) Testar acesso sem token.<br>5) Comparar estrutura das respostas. | As respostas devem possuir formato previsível, com status, mensagem e detalhes suficientes para o frontend exibir feedback. | 🟡 Planejado | Existe |
| RT-18 | ⚠️ Média | Teste de fluxo completo ponta a ponta | Validar o fluxo principal do sistema do cadastro até o alerta de validade. | Backend e frontend rodando, banco configurado e integração finalizada. | Usuário novo, inventário, produto e lote com validade próxima. | 1) Cadastrar usuário.<br>2) Fazer login.<br>3) Criar inventário.<br>4) Criar produto.<br>5) Criar lote.<br>6) Consultar status do lote.<br>7) Abrir alertas.<br>8) Validar dados exibidos. | O sistema deve permitir o fluxo completo `Usuário → Inventário → Produto → Lote → Alertas`, com autenticação e dados corretos. | 🟡 Planejado | Não existe |

---

## 📎 Referência dos prints

| Print | RT relacionado | Arquivo |
| --- | --- | --- |
| Print 01 — Testes do backend no terminal | RT-01, RT-02, RT-03, RT-04, RT-05, RT-06, RT-07, RT-08, RT-09, RT-10 | `prints/Print_01_RT-01_a_RT-10_backend_mvn_test_build_success.jpeg` |
| Print 02 — Testes unitários passando na IDE | RT-01, RT-02, RT-03, RT-04, RT-05, RT-06, RT-07, RT-08, RT-09, RT-10 | `prints/Print_02_RT-01_a_RT-10_testes_unitarios_IDE.jpeg` |
| Print 03 — Login com JWT via API | RT-11 | `prints/Print_03_RT-11_login_JWT_API.jpeg` |
| Print 04 — Consulta de inventários via API | RT-07, RT-11, RT-15, RT-17 | `prints/Print_04_RT-07_RT-11_RT-15_RT-17_consulta_inventarios_API.jpeg` |
| Print 05 — Consulta de alertas do mês via API | RT-09, RT-16, RT-17 | `prints/Print_05_RT-09_RT-16_RT-17_alertas_mes_API.jpeg` |
| Print 06 — Consulta de alertas da semana via API | RT-09, RT-16, RT-17 | `prints/Print_06_RT-09_RT-16_RT-17_alertas_semana_API.jpeg` |
| Print 07 — Tela de cadastro no frontend | RT-12 | `prints/Print_07_RT-12_tela_cadastro_frontend.jpeg` |
| Print 08 — Tela de login no frontend | RT-13 | `prints/Print_08_RT-13_tela_login_frontend.jpeg` |
| Print 09 — Funcionalidades de estoque no frontend | RT-15 | `prints/Print_09_RT-15_funcionalidades_estoque_frontend.jpeg` |
| Print 10 — Alertas de validade no frontend | RT-16 | `prints/Print_10_RT-16_alertas_validade_frontend.jpeg` |
| Print 11 — Landing page — início | Geral | `prints/Print_11_GERAL_landing_page_inicio.jpeg` |
| Print 12 — Landing page — rodapé | Geral | `prints/Print_12_GERAL_landing_page_footer.jpeg` |

---

## 🖼️ Prints de evidência

### Print 01 — Testes do backend no terminal

**RT relacionado:** RT-01, RT-02, RT-03, RT-04, RT-05, RT-06, RT-07, RT-08, RT-09, RT-10

![Print 01 — Testes do backend no terminal](prints/Print_01_RT-01_a_RT-10_backend_mvn_test_build_success.jpeg)

### Print 02 — Testes unitários passando na IDE

**RT relacionado:** RT-01, RT-02, RT-03, RT-04, RT-05, RT-06, RT-07, RT-08, RT-09, RT-10

![Print 02 — Testes unitários passando na IDE](prints/Print_02_RT-01_a_RT-10_testes_unitarios_IDE.jpeg)

### Print 03 — Login com JWT via API

**RT relacionado:** RT-11

![Print 03 — Login com JWT via API](prints/Print_03_RT-11_login_JWT_API.jpeg)

### Print 04 — Consulta de inventários via API

**RT relacionado:** RT-07, RT-11, RT-15, RT-17

![Print 04 — Consulta de inventários via API](prints/Print_04_RT-07_RT-11_RT-15_RT-17_consulta_inventarios_API.jpeg)

### Print 05 — Consulta de alertas do mês via API

**RT relacionado:** RT-09, RT-16, RT-17

![Print 05 — Consulta de alertas do mês via API](prints/Print_05_RT-09_RT-16_RT-17_alertas_mes_API.jpeg)

### Print 06 — Consulta de alertas da semana via API

**RT relacionado:** RT-09, RT-16, RT-17

![Print 06 — Consulta de alertas da semana via API](prints/Print_06_RT-09_RT-16_RT-17_alertas_semana_API.jpeg)

### Print 07 — Tela de cadastro no frontend

**RT relacionado:** RT-12

![Print 07 — Tela de cadastro no frontend](prints/Print_07_RT-12_tela_cadastro_frontend.jpeg)

### Print 08 — Tela de login no frontend

**RT relacionado:** RT-13

![Print 08 — Tela de login no frontend](prints/Print_08_RT-13_tela_login_frontend.jpeg)

### Print 09 — Funcionalidades de estoque no frontend

**RT relacionado:** RT-15

![Print 09 — Funcionalidades de estoque no frontend](prints/Print_09_RT-15_funcionalidades_estoque_frontend.jpeg)

### Print 10 — Alertas de validade no frontend

**RT relacionado:** RT-16

![Print 10 — Alertas de validade no frontend](prints/Print_10_RT-16_alertas_validade_frontend.jpeg)

### Print 11 — Landing page — início

**RT relacionado:** Geral

![Print 11 — Landing page — início](prints/Print_11_GERAL_landing_page_inicio.jpeg)

### Print 12 — Landing page — rodapé

**RT relacionado:** Geral

![Print 12 — Landing page — rodapé](prints/Print_12_GERAL_landing_page_footer.jpeg)
