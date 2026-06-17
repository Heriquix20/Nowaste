# 🧪✅ Template - Teste de Usabilidade — noWaste

Teste de usabilidade criado com base nos roteiros do projeto **noWaste**.  
O teste foi planejado como **remoto**, **sem gravação**, usando apenas **formulário online**, **notas do observador** e **registros textuais de evidência**.

---

## 🎯 Objetivo do teste

| Item | Preencher |
|---|---|
| **Objetivo** | Avaliar se pessoas comuns conseguem usar o noWaste para executar o fluxo principal: cadastrar usuário, fazer login, criar inventário, cadastrar produto, cadastrar lote e consultar alertas de validade. |
| **Hipótese (o que esperamos)** | Espera-se que os participantes consigam completar as tarefas principais sem ajuda direta. A equipe espera encontrar possíveis dúvidas nos termos “inventário”, “lote”, “validade” e nos alertas de vencimento. |
| **Escopo (telas/funcionalidades)** | Tela inicial, cadastro, login, painel de inventários, cadastro/listagem de produtos, cadastro de lote e alertas de validade. |

---

## 2) 👥 Participantes (perfil)

| ID | Perfil | Experiência | Contexto | Observações |
|---|---|---|---|---|
| P1 | Estudante| Leigo / Médio / Avançado: **Médio** | Presencial | Conhecimento básico em sistemas. |
| P2 | atendente | Leigo / Médio / Avançado: **Leigo** | Remoto | Usa celular e computador para tarefas básicas, mas não costuma usar sistemas de estoque. |
| P3 | Auxiliar administrativo(a) | Leigo / Médio / Avançado: **Médio** | Remoto | Já preenche cadastros em sistemas, mas não tem conhecimento técnico. |

---

## ⚙️ Ambiente e preparação

| Item | Descrição |
|---|---|
| **Dispositivo** | Notebook / PC |
| **Ambiente** | Casa / Online |
| **Versão testada** | dev tag v0.1.0  |
| **Gravação** | Sim.|
| **Evidências** | Formulário pós-teste, notas do observador, prints já anexados no repositório e checklist das tarefas executadas. |

---

## 🧩 Tarefas (roteiro do teste)

| Tarefa | Roteiro relacionado | Descrição | Sucesso (critério) | Tempo alvo | Prioridade |
|---|---|---|---|---|---|
| T1 | RT-01 | Cadastrar uma nova conta no noWaste. | Usuário criado com nome, e-mail e senha. | 03:00 | 🔥 Alta |
| T2 | RT-02 | Fazer login com a conta criada. | Usuário entra no sistema e acessa a área privada. | 02:00 | 🔥 Alta |
| T3 | RT-03 | Criar um inventário chamado **Estoque Mercado**. | Inventário aparece na listagem. | 03:00 | 🔥 Alta |
| T4 | RT-05 | Cadastrar o produto **Leite Integral**, categoria **Laticínios**, marca **Italac**, peso **1 kg**. | Produto aparece dentro do inventário. | 04:00 | 🔥 Alta |
| T5 | RT-07 | Cadastrar um lote do produto com quantidade 10 e validade próxima. | Lote é criado e vinculado ao produto. | 04:00 | 🔥 Alta |
| T6 | RT-09 | Localizar os alertas de validade. | Participante encontra e entende os alertas de lote vencido/próximo do vencimento. | 03:00 | 🔥 Alta |

---

## 📊 Resultados por participante e tarefa

**Legenda:** ✅ Concluiu • ⚠️ Concluiu com dificuldade/ajuda • ❌ Não concluiu

| Participante | Tarefa | Status | Tempo | Dificuldade observada | Comentário participante | Evidência |
|---|---|---|---|---|---|---|
| P1 | T1 | ✅ | 02:20 | Nenhuma dificuldade crítica. | “O cadastro é parecido com outros sites.” | Formulário P1 + EVID-USAB-01 |
| P1 | T2 | ✅ | 01:15 | Nenhuma. | “Consegui entrar rápido.” | Formulário P1 + EVID-USAB-02 |
| P1 | T3 | ✅ | 02:40 | Pequena dúvida sobre o nome “inventário”. | “Eu entendi melhor pensando como estoque.” | Nota OBS-01 + EVID-USAB-03 |
| P1 | T4 | ✅ | 03:30 | Nenhuma crítica. | “Os campos do produto são claros.” | Formulário P1 + EVID-USAB-04 |
| P1 | T5 | ⚠️ | 04:20 | Dúvida sobre onde colocar a validade do lote. | “Precisei ler com calma para entender lote.” | Nota OBS-02 + EVID-USAB-05 |
| P1 | T6 | ✅ | 02:30 | Nenhuma crítica. | “Os alertas fazem sentido para evitar perder produto.” | Formulário P1 + EVID-USAB-06 |
| P2 | T1 | ✅ | 02:50 | Dúvida se o cadastro tinha sido finalizado. | “Faltou aparecer uma confirmação mais visível.” | Formulário P2 + EVID-USAB-01 |
| P2 | T2 | ✅ | 01:50 | Nenhuma. | “Login normal, consegui entrar.” | Formulário P2 + EVID-USAB-02 |
| P2 | T3 | ⚠️ | 03:40 | Confundiu inventário com produto. | “Achei que já era para cadastrar o leite primeiro.” | Nota OBS-03 + EVID-USAB-03 |
| P2 | T4 | ⚠️ | 04:30 | Dúvida no campo de peso. | “Não sabia se colocava 1 kg ou 1000 gramas.” | Formulário P2 + EVID-USAB-04 |
| P2 | T5 | ⚠️ | 05:00 | Maior dificuldade no teste. | “A parte do lote foi a que eu mais demorei.” | Nota OBS-04 + EVID-USAB-05 |
| P2 | T6 | ⚠️ | 03:20 | Alertas poderiam estar mais destacados. | “Achei, mas poderia chamar mais atenção.” | Formulário P2 + EVID-USAB-06 |
| P3 | T1 | ✅ | 01:40 | Nenhuma. | “Cadastro objetivo.” | Formulário P3 + EVID-USAB-01 |
| P3 | T2 | ✅ | 01:10 | Nenhuma. | “Login funcionou sem problema.” | Formulário P3 + EVID-USAB-02 |
| P3 | T3 | ✅ | 02:10 | Nenhuma. | “A criação do inventário foi tranquila.” | Formulário P3 + EVID-USAB-03 |
| P3 | T4 | ✅ | 03:00 | Nenhuma crítica. | “Produto foi fácil de cadastrar.” | Formulário P3 + EVID-USAB-04 |
| P3 | T5 | ✅ | 03:50 | Pequena dúvida no código automático do lote. | “Gostei do código automático, mas poderia explicar.” | Nota OBS-05 + EVID-USAB-05 |
| P3 | T6 | ✅ | 02:20 | Termos de status poderiam ser mais amigáveis. | “Eu trocaria WARNING por ‘vence em breve’.” | Formulário P3 + EVID-USAB-06 |

---

## ⭐ Questionário rápido (pós-teste)

| Participante | Facilidade (1-5) | Clareza (1-5) | Velocidade (1-5) | O que mais gostou? | O que melhoraria? |
|---|---:|---:|---:|---|---|
| P1 | 4 | 4 | 4 | A ideia dos alertas de validade. | Explicar melhor o que é lote e inventário. |
| P2 | 3 | 3 | 3 | Poder controlar produtos que vencem. | Mensagens mais visíveis e formulário de lote mais guiado. |
| P3 | 5 | 4 | 5 | Fluxo simples e direto. | Trocar termos técnicos por palavras mais comuns. |

---

## 🧠 Achados e melhorias (priorização)

| Achado | O que aconteceu | Impacto | Frequência | Prioridade | Evidência recomendada | Como validar |
|---|---|---|---|---|---|---|
| Termo “inventário” pode confundir usuários comuns. | P1 e P2 entenderam melhor quando associaram inventário a estoque. | Médio | P1, P2 | ⚠️ Média | Comentários do formulário e notas OBS-01/OBS-03. | Testar rótulo “Inventário/Estoque” em nova rodada. |
| Cadastro de lote foi a tarefa mais difícil. | P1 e P2 tiveram dúvida em quantidade, validade e conceito de lote. | Alto | P1, P2 | 🔥 Alta | Notas OBS-02/OBS-04 e tempos acima do alvo. | Adicionar texto de ajuda no modal de lote e repetir T5. |
| Feedback de cadastro poderia ser mais visível. | P2 não teve certeza se o cadastro foi concluído. | Médio | P2 | ⚠️ Média | Comentário do formulário P2. | Inserir toast/mensagem de sucesso e medir nova percepção. |
| Alertas precisam de maior destaque visual. | P2 encontrou os alertas, mas achou pouco chamativo. | Médio | P2 | 🔥 Alta | Comentário do formulário P2 e EVID-USAB-06. | Usar cards, cores e etiquetas de prioridade. |
| Status técnico pode dificultar entendimento. | P3 sugeriu trocar WARNING por linguagem comum. | Médio | P3 | ⚠️ Média | Comentário do formulário P3. | Traduzir status para “Vencido”, “Vence em breve” e “Dentro da validade”. |

---

## 📎 Evidências

| Evidência | Tipo | Descrição |
|---|---|---|
| EVID-USAB-01 | Print + formulário | Tela de cadastro usada na tarefa T1. Link: https://github.com/user-attachments/assets/1c0cfea1-6d83-4cb6-b8a3-e0baf94ab63d |
| EVID-USAB-02 | Print + formulário | Tela de login usada na tarefa T2. Link: https://github.com/user-attachments/assets/811ad0bc-5ff5-4a45-8aa2-e048a5b06f16 |
| EVID-USAB-03 | Print + nota | Painel/listagem de inventários usado na tarefa T3. Link: https://github.com/user-attachments/assets/bbcc88c6-fa64-45e4-aeeb-a51eebe4a723 |
| EVID-USAB-04 | Formulário + nota | Registro textual do cadastro do produto “Leite Integral”. |
| EVID-USAB-05 | Nota do observador | Registro textual das dúvidas no cadastro de lote. |
| EVID-USAB-06 | Print + formulário | Tela de alertas de validade usada na tarefa T6. Link: https://github.com/user-attachments/assets/490404fa-d6e3-4455-b07e-7b4f1f9abf5e |

## 📎 Vídeo 

https://drive.google.com/file/d/1tPADlr7lKcdgCoTbQx3oDH0VQ_3How8-/view?usp=sharing 

---

## ✅ Conclusão e decisão

| Item | Resultado |
|---|---|
| **Principais pontos positivos** | Os participantes entenderam a proposta do noWaste e conseguiram executar o fluxo principal. Cadastro e login foram considerados simples. A funcionalidade de alertas foi vista como útil para evitar desperdício. |
| **Principal dificuldade** | O cadastro de lote foi a etapa com mais dúvidas, principalmente por envolver quantidade, validade e conceito de lote. |
| **Top 3 melhorias** | 1. Explicar melhor “lote” e “inventário/estoque”.<br>2. Melhorar feedback visual após cadastros.<br>3. Destacar alertas e trocar status técnicos por linguagem comum. |
| **Go/No-Go para entrega** | **Go com ajustes.** O fluxo principal funciona para usuários comuns, mas recomenda-se melhorar textos, feedbacks e alertas. |
| **Links de evidência** | Prints do repositório (readme e pasta de evidencias) e video gravado, formulário pós-teste, notas do observador e checklist das tarefas. |
