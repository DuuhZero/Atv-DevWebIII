## Como rodar aplicação atividade 5

### 1º Passo:

```
Confira se Java e Javac estão na versão 17
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

## Como Testar os dados

### Clientes

Como ver os dados do cliente
```
GET http://localhost:8080/cliente/clientes
```

Como ver os dados de um cliente especifico
```
http://localhost:8080/cliente/clientes/{id}
```

Como realizar um post de um cliente
```
POST http://localhost:8080/cliente/cadastro
Content-Type: application/json

{
  "nome": "João Silva",
  "nomeSocial": "João",
  "dataNascimento": "1990-01-01",
  "dataCadastro": "2023-01-01",
  "documentos": [
    {
      "tipo": "CPF",
      "numero": "12345678901"
    }
  ],
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro",
    "rua": "Rua Principal",
    "numero": "100",
    "codigoPostal": "01001000",
    "informacoesAdicionais": "Apto 101"
  },
  "telefones": [
    {
      "ddd": "11",
      "numero": "999999999"
    }
  ]
}
```

Como atualizar as informações de um cliente
```
PUT http://localhost:8080/cliente/atualizar
Content-Type: application/json

{
  "id": 1,
  "nome": "João Silva Santos",
  "nomeSocial": "Joãozinho",
  "dataNascimento": "1990-01-01",
  "dataCadastro": "2023-01-01"
}
```

Como deletar um cliente
```
DELETE http://localhost:8080/cliente/excluir/{id}
```

### Documento 

Listar todos documentos
```
GET http://localhost:8080/documento/documentos
```

Listar um documento especifico
```
GET http://localhost:8080/documento/documento/{id}
```

Cadastrar um documento
```
POST http://localhost:8080/documento/cadastro

Content-Type: application/json
{
  "tipo": "RG",
  "numero": "12345678"
}
```

Atualizar um documento
```
PUT http://localhost:8080/documento/atualizar


{
  "id": 1,
  "tipo": "CNH",
  "numero": "987654321"
}
```

Deletar um documento
```
DELETE http://localhost:8080/documento/excluir/1
```

### Endereço

Listar todos os endereços
```
GET http://localhost:8080/endereco/enderecos
```

Listar um endereço especifico
```
GET http://localhost:8080/endereco/endereco/{id}
```

Cadastrar um endereço
```
POST http://localhost:8080/endereco/cadastro


{
  "estado": "RJ",
  "cidade": "Rio de Janeiro",
  "bairro": "Copacabana",
  "rua": "Avenida Atlântica",
  "numero": "500",
  "codigoPostal": "22070010",
  "informacoesAdicionais": "Bloco B"
}
```

Atualizar um endereço
```
PUT http://localhost:8080/endereco/atualizar

{
  "id": 1,
  "estado": "RJ",
  "cidade": "Rio de Janeiro",
  "bairro": "Ipanema",
  "rua": "Rua Visconde de Pirajá",
  "numero": "300",
  "codigoPostal": "22410002"
}
```

Deletar um endereço
```
DELETE http://localhost:8080/endereco/excluir/{id}
```

### Telefone

Listar todos os telefones
```
GET http://localhost:8080/telefone/telefones
```

Listar um telefone especifico
```
GET http://localhost:8080/telefone/telefone/{id}
```

Cadastrar um telefone
```
POST http://localhost:8080/telefone/cadastro


{
  "ddd": "21",
  "numero": "987654321"
}
```

Atualizar um telefone
```
PUT http://localhost:8080/telefone/atualizar

{
  "id": 1,
  "ddd": "21",
  "numero": "987654322"
}
```

Deletar um telefone
```
DELETE http://localhost:8080/telefone/excluir/{id}
```
