# 📋 Lista de Requisitos Não Funcionais — noWaste

| ID | Requisito não funcional | Descrição |
|---|---|---|
| RNF-01 | Segurança de autenticação | O sistema deve utilizar autenticação baseada em JWT para proteger as rotas privadas. |
| RNF-02 | Segurança de senha | As senhas dos usuários não devem ser armazenadas em texto puro. |
| RNF-03 | Controle de acesso | O sistema deve impedir que um usuário acesse dados pertencentes a outro usuário. |
| RNF-04 | Organização em camadas | O backend deve estar organizado em camadas como controller, service, repository, domain, DTO e exceptions. |
| RNF-05 | Manutenibilidade | O código deve ser estruturado de forma clara, facilitando futuras alterações e expansão do projeto. |
| RNF-06 | Padronização de DTOs | A API deve utilizar DTOs para entrada e saída de dados, evitando exposição direta das entidades. |
| RNF-07 | Padronização de respostas | A API deve retornar respostas consistentes para sucesso e erro, facilitando o tratamento pelo frontend. |
| RNF-08 | Testabilidade | As regras de negócio principais devem possuir testes unitários automatizados. |
| RNF-09 | Cobertura de regras críticas | Os testes devem cobrir regras como status de validade, filtros, geração de código, autenticação e proteção de usuário. |
| RNF-10 | Usabilidade | As telas devem ser simples, claras e compreensíveis para usuários que precisam controlar estoque. |
| RNF-11 | Responsividade inicial | O frontend deve buscar adaptação visual básica para diferentes tamanhos de tela. |
| RNF-12 | Desempenho em consultas | Filtros e listagens devem responder em tempo aceitável para uso em ambiente local ou pequeno comércio. |
| RNF-13 | Persistência de dados | O sistema deve armazenar os dados em banco relacional MySQL. |
| RNF-14 | Portabilidade de ambiente | O projeto deve poder ser executado em ambiente local com Java, Maven, MySQL e frontend configurado. |
| RNF-15 | Configuração externa | Dados sensíveis e variáveis de ambiente devem ser configurados fora do código-fonte, como em arquivo `.env`. |
| RNF-16 | Escalabilidade inicial | A arquitetura deve permitir evolução futura para novas funcionalidades, como relatórios, categorias globais e notificações. |
| RNF-17 | Clareza na documentação | O README deve explicar objetivo, tecnologias, instalação, execução, rotas, testes e situação da Sprint. |
| RNF-18 | Evidenciabilidade | O projeto deve possuir prints, logs ou vídeos comprovando execução de testes, API e telas do frontend. |
| RNF-19 | Confiabilidade das regras de validade | O sistema deve calcular status e alertas de forma consistente, evitando informações incorretas sobre vencimentos. |
| RNF-20 | Integridade dos dados | O sistema deve validar dados obrigatórios e impedir inconsistências, como peso mínimo maior que peso máximo ou e-mail duplicado. |
| RNF-21 | Compatibilidade com API REST | O backend deve expor endpoints HTTP claros, organizados por recurso e compatíveis com consumo pelo frontend. |
| RNF-22 | Legibilidade do código | Classes, métodos, pacotes e responsabilidades devem seguir nomes compreensíveis e coerentes com o domínio. |
| RNF-23 | Facilidade de demonstração | O sistema deve permitir apresentação do fluxo principal durante a entrega da Sprint. |
| RNF-24 | Evolução incremental | O projeto deve seguir a metodologia Scrum, permitindo entregas por Sprint e acompanhamento via Kanban. |
| RNF-25 | Licenciamento | O projeto deve manter a licença definida no repositório. |

---
