# language: pt

Funcionalidade: Alertas de validade

  Cenário: Listar alertas de lotes vencidos ou que vencem no mês atual
    Dado que existe um usuário autenticado
    E que o usuário possui um inventário cadastrado
    E que o usuário possui um produto "Leite" com peso 1000 gramas
    E que existe um lote vencido desse produto com quantidade 2
    E que existe um lote desse produto com quantidade 3 e validade ainda neste mês
    Quando ele consulta os alertas de lotes vencidos
    Então a resposta de alertas deve ser 200
    E a lista de alertas vencidos deve conter pelo menos 1 item
    Quando ele consulta os alertas de lotes do mês atual
    Então a resposta de alertas deve ser 200
    E a lista de alertas do mês atual deve conter pelo menos 1 item