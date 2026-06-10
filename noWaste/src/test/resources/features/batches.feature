# language: pt

Funcionalidade: Controle de lotes

  Cenário: Cadastrar lote e gerar código automático no padrão LT-NOME_DO_PRODUTO-001
    Dado que existe um usuário autenticado
    E que o usuário possui um inventário cadastrado
    E que o usuário possui um produto "Arroz" com peso 1000 gramas
    Quando ele cadastra um lote com quantidade 10 e validade em 15 dias
    Então a resposta do lote deve ser 201
    E o lote retornado deve ter código "LT-ARROZ-001"

  Cenário: Calcular status WARNING para lote que vence em até 7 dias
    Dado que existe um usuário autenticado
    E que o usuário possui um inventário cadastrado
    E que o usuário possui um produto "Feijao" com peso 500 gramas
    E que existe um lote desse produto com quantidade 4 e validade em 5 dias
    Quando ele consulta os lotes do produto
    Então a resposta do lote deve ser 200
    E o primeiro lote retornado deve ter status "WARNING"