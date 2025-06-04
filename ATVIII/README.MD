## Como rodar aplicação atividade 3

### 1º Passo:

```
Confira se Java e Javac estão na versão 17
Confira suas credenciais no application properties
```

### 2º Passo:
Execute estes comandos abaixo:
```
mvn clean install
mvn spring-boot:run
```

### 3º Passo
```
Confira no terminal se a aplicação Inicializou
```

## Utilizando algumas rotas

## Empresa
### Criar uma empresa:
```

POST http://localhost:8080/empresas
{
    "razaoSocial": "Auto Mecânica Fast Service Ltda",
    "nomeFantasia": "Fast Service - Centro Automotivo",
    "telefones": [
        {
            "ddd": "011",
            "numero": "987654321"
        },
        {
            "ddd": "011",
            "numero": "40028922"
        }
    ],
    "endereco": {
        "estado": "São Paulo",
        "cidade": "São Paulo",
        "bairro": "Vila Mariana",
        "rua": "Rua Domingos de Morais",
        "numero": "1000",
        "codigoPostal": "04010-000",
        "informacoesAdicionais": "Próximo ao metrô Vila Mariana"
    },
    "mercadorias": [
        {
            "nome": "Óleo Lubrificante 10W40",
            "quantidade": 50,
            "valor": 35.90,
            "descricao": "Óleo lubrificante sintético para motores a gasolina",
            "validade": "2025-12-31T00:00:00.000+00:00",
            "fabricao": "2024-01-15T00:00:00.000+00:00"
        }
    ],
    "servicos": [
        {
            "nome": "Troca de óleo",
            "valor": 80.00,
            "descricao": "Troca de óleo e filtro"
        },
        {
            "nome": "Revisão básica",
            "valor": 250.00,
            "descricao": "Revisão básica de 10 itens"
        }
    ]
}
```

### Listar todas empresas:
```
GET http://localhost:8080/empresas
```

### Obter empresa específica:
```
GET http://localhost:8080/empresas/{id}
```

### Atualizar empresa:
```
PUT http://localhost:8080/empresas/{id}

{
    "razaoSocial": "Nova Razão Social Ltda",
    "nomeFantasia": "Novo Nome Fantasia"
}
```

### Excluir empresa:
```
DELETE http://localhost:8080/empresas/{id}

```

## Usuário
### Criar um Usuário:
```
POST http://localhost:8080/usuarios

{
    "nome": "Maria Oliveira",
    "nomeSocial": "Maria",
    "email": "maria.oliveira@cliente.com",
    "perfis": ["CLIENTE"],
    "telefones": [
        {
            "ddd": "011",
            "numero": "912345678"
        }
    ],
    "documentos": [
        {
            "tipo": "CPF",
            "numero": "98765432109",
            "dataEmissao": "2020-01-15T00:00:00.000+00:00"
        }
    ],
    "endereco": {
        "estado": "São Paulo",
        "cidade": "São Paulo",
        "bairro": "Moema",
        "rua": "Av. Ibirapuera",
        "numero": "1000",
        "codigoPostal": "04029-000"
    },
    "veiculos": [
        {
            "placa": "ABC1D23",
            "modelo": "Toyota Corolla",
            "tipo": "SEDA",
            "ano": 2020
        }
    ]
}

```
### Listar todos usuários:
```
GET http://localhost:8080/usuarios
```

### Obter usuário específico:
```
GET http://localhost:8080/usuarios/{id}
```

### Atualizar usuário:
```
PUT http://localhost:8080/usuarios/{id}

{
    "nome": "Maria Oliveira Silva",
    "email": "maria.silva@novoemail.com"
}
```

### Excluir usuário:
```
DELETE http://localhost:8080/usuarios/{id}
```
### Associar tal usuário a uma empresa:
```
POST http://localhost:8080/usuarios/{empresaId}/associar

{
 "id": 1
}
```

## Mercadorias

### Criar mercadoria:
```
POST http://localhost:8080/mercadorias


{
    "nome": "Óleo Lubrificante 5W30",
    "quantidade": 30,
    "valor": 42.90,
    "descricao": "Óleo sintético para motores modernos"
}
```

### Listar todas mercadorias:
```
GET http://localhost:8080/mercadorias
```

### Obter mercadoria específica:
```
GET http://localhost:8080/mercadorias/{id}
```

### Atualizar mercadoria:
```
PUT http://localhost:8080/mercadorias/{id}

{
    "nome": "Óleo Lubrificante 5W30 Premium",
    "quantidade": 35,
    "valor": 49.90
}
```

### Excluir mercadoria:
```
DELETE http://localhost:8080/mercadorias/{id}
```

## Serviços

### Criar serviço:
```
POST http://localhost:8080/servicos

{
    "nome": "Alinhamento e Balanceamento",
    "valor": 120.00,
    "descricao": "Alinhamento e balanceamento completo"
}
```

### Listar todos serviços:
```
GET http://localhost:8080/servicos
```

### Obter serviço específico:
```
GET http://localhost:8080/servicos/{id}
```

### Atualizar serviço:
```
PUT http://localhost:8080/servicos/{id}

{
    "nome": "Alinhamento 3D",
    "valor": 150.00,
    "descricao": "Alinhamento 3D completo"
}
```

### Excluir serviço:
```
DELETE http://localhost:8080/servicos/{id}
```

## Veiculos

### Criar veículo:
```
POST http://localhost:8080/veiculos


{
    "placa": "XYZ9A87",
    "modelo": "Honda Civic",
    "tipo": "SEDA",
    "ano": 2021
}
```

### Listar todos veículos:
```
GET http://localhost:8080/veiculos
```
### Obter veículo específico:
```
GET http://localhost:8080/veiculos/{id}
```

### Atualizar veículo:
```
PUT http://localhost:8080/veiculos/{id}


{
    "placa": "XYZ9A87",
    "modelo": "Honda Civic Touring",
    "ano": 2022
}
```

### Excluir veículo:
```
DELETE http://localhost:8080/veiculos/{id}
```

## Vendas

### Criar venda:
```
POST http://localhost:8080/vendas


{
    "cliente": {
        "id": 1
    },
    "funcionario": {
        "id": 2
    },
    "veiculo": {
        "id": 1
    },
    "servicos": [
        {
            "id": 1
        }
    ],
    "mercadorias": [
        {
            "id": 1,
            "quantidade": 1
        }
    ]
}
```

### Listar todas vendas:
```
GET http://localhost:8080/vendas
```

### Obter venda específica:
```
GET http://localhost:8080/vendas/{id}
```

### Atualizar venda:
```
PUT http://localhost:8080/vendas/{id}

{
    "cliente": {
        "id": 1
    },
    "funcionario": {
        "id": 3
    },
    "servicos": [
        {
            "id": 2
        }
    ]
}
```

### Excluir venda:
```
DELETE http://localhost:8080/vendas/{id}
```

### Todas as rotas também podem ser conferidas através dos links quando se faz cada requisição.