# 📋 Lista de Requisitos Funcionais — noWaste


| ID | Requisito funcional | Descrição |
|---|---|---|
| RF-01 | Cadastro de usuário | O sistema deve permitir o cadastro de usuários com nome, e-mail e senha. |
| RF-02 | Login de usuário | O sistema deve permitir que o usuário realize login por meio de e-mail e senha. |
| RF-03 | Autenticação com JWT | O sistema deve gerar um token JWT após o login bem-sucedido. |
| RF-04 | Proteção de rotas | O sistema deve restringir o acesso às rotas privadas apenas a usuários autenticados. |
| RF-05 | Gerenciamento de inventários | O sistema deve permitir criar, listar, consultar, atualizar e excluir inventários vinculados ao usuário autenticado. |
| RF-06 | Bloqueio de inventário duplicado | O sistema deve impedir que o mesmo usuário cadastre inventários com nomes duplicados. |
| RF-07 | Filtro e ordenação de inventários | O sistema deve permitir filtrar inventários por nome e ordenar os resultados. |
| RF-08 | Cadastro de produtos | O sistema deve permitir cadastrar produtos dentro de um inventário. |
| RF-09 | Consulta de produtos | O sistema deve permitir listar e consultar produtos cadastrados em um inventário. |
| RF-10 | Atualização de produtos | O sistema deve permitir atualizar os dados de um produto existente. |
| RF-11 | Exclusão de produtos | O sistema deve permitir excluir produtos cadastrados em um inventário. |
| RF-12 | Cadastro de peso do produto | O sistema deve permitir informar o peso do produto em gramas ou quilogramas. |
| RF-13 | Conversão de peso | O sistema deve converter automaticamente pesos informados em quilogramas para gramas. |
| RF-14 | Filtros de produtos | O sistema deve permitir filtrar produtos por nome, categoria, marca e intervalo de peso. |
| RF-15 | Ordenação de produtos | O sistema deve permitir ordenar produtos por peso. |
| RF-16 | Validação de peso mínimo e máximo | O sistema deve retornar erro quando o peso mínimo informado for maior que o peso máximo. |
| RF-17 | Cadastro de lotes | O sistema deve permitir cadastrar lotes vinculados a um produto. |
| RF-18 | Consulta de lotes | O sistema deve permitir listar e consultar lotes cadastrados para um produto. |
| RF-19 | Atualização de lotes | O sistema deve permitir atualizar dados de um lote existente. |
| RF-20 | Exclusão de lotes | O sistema deve permitir excluir lotes cadastrados. |
| RF-21 | Geração automática de código de lote | O sistema deve gerar automaticamente o código do lote no padrão `LT-NOME_DO_PRODUTO-001`. |
| RF-22 | Sequência de código por produto | O sistema deve manter a sequência de códigos de lote separada por produto. |
| RF-23 | Cálculo de dias restantes | O sistema deve calcular quantos dias faltam para o vencimento de um lote. |
| RF-24 | Cálculo de peso total do lote | O sistema deve calcular o peso total do lote com base no peso do produto e na quantidade cadastrada. |
| RF-25 | Cálculo de status de validade | O sistema deve calcular automaticamente o status do lote conforme a data de vencimento. |
| RF-26 | Status de lote vencido | O sistema deve classificar lotes com data vencida como `EXPIRED`. |
| RF-27 | Status de alerta próximo | O sistema deve classificar lotes que vencem em até 7 dias como `WARNING`. |
| RF-28 | Status de alerta mensal | O sistema deve classificar lotes que vencem entre 8 e 30 dias como `MONTH_WARNING`. |
| RF-29 | Status de lote válido | O sistema deve classificar lotes com vencimento acima de 30 dias como `OK`. |
| RF-30 | Status desconhecido | O sistema deve classificar lotes sem data de validade como `UNKNOWN`. |
| RF-31 | Filtros de lotes | O sistema deve permitir filtrar lotes por código, status, intervalo de validade e quantidade. |
| RF-32 | Ordenação de lotes | O sistema deve permitir ordenar lotes por data de vencimento. |
| RF-33 | Alertas de lotes vencidos | O sistema deve disponibilizar consulta de lotes vencidos. |
| RF-34 | Alertas de lotes próximos do vencimento | O sistema deve disponibilizar consulta de lotes que vencem no mês atual. |
| RF-35 | Isolamento dos dados por usuário | O sistema deve garantir que cada usuário visualize e altere apenas seus próprios dados. |
| RF-36 | Criptografia de senha | O sistema deve armazenar senhas de forma criptografada. |
| RF-37 | Bloqueio de e-mail duplicado | O sistema deve impedir o cadastro de usuários com e-mail já existente. |
| RF-38 | Tratamento de erros | O sistema deve retornar mensagens de erro adequadas para dados inválidos, recursos inexistentes e acesso não autorizado. |
| RF-39 | Tela inicial / Landing page | O sistema deve possuir uma tela inicial apresentando a proposta do noWaste. |
| RF-40 | Tela de cadastro | O frontend deve possuir uma tela para cadastro de usuários. |
| RF-41 | Tela de login | O frontend deve possuir uma tela para login de usuários. |
| RF-42 | Tela de inventários | O frontend deve possuir uma tela para visualização e gerenciamento de inventários. |
| RF-43 | Tela de produtos | O frontend deve possuir uma tela para visualização e gerenciamento de produtos. |
| RF-44 | Formulário de produto | O frontend deve possuir formulário para criação e edição de produtos. |
| RF-45 | Tela de lotes | O frontend deve possuir uma tela para visualização e gerenciamento de lotes. |
| RF-46 | Formulário de lote | O frontend deve possuir formulário para criação e edição de lotes. |
| RF-47 | Dashboard de alertas | O frontend deve exibir alertas de produtos vencidos ou próximos do vencimento. |
| RF-48 | Estados visuais no frontend | O frontend deve apresentar telas, botões, formulários e mensagens compreensíveis ao usuário. |
| RF-49 | Integração com API REST | O frontend deve consumir os endpoints do backend para autenticação, inventários, produtos, lotes e alertas. |
| RF-50 | Fluxo completo do sistema | O sistema deve permitir o fluxo: usuário → inventário → produto → lote → alertas. |

---

---
