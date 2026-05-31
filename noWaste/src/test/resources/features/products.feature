# language: pt

Funcionalidade: Controle de produtos

  Cenário: Cadastrar produto com peso em kg e armazenar em gramas
    Dado que existe um usuário autenticado
    E que o usuário possui um inventário cadastrado
    Quando ele cadastra um produto com nome "Arroz" categoria "Graos" marca "Tio Joao" peso 5 unidade "kg"
    Então a resposta do produto deve ser 201
    E o produto retornado deve ter peso 5000 gramas
    E o produto retornado deve ter unidade "g"

  Cenário: Bloquear filtro de produto quando minWeight for maior que maxWeight
    Dado que existe um usuário autenticado
    E que o usuário possui um inventário cadastrado
    Quando ele consulta produtos com minWeight 3000 e maxWeight 500
    Então a resposta do produto deve ser 400
    E a mensagem de erro deve conter "peso"