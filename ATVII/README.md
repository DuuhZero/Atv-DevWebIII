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

### Cliente

Obter Cliente por ID
```
GET http://localhost:8080/clientes/1
```

Obter Todos os Clientes
```
GET http://localhost:8080/clientes
```

Cadastrar Cliente
```
POST http://localhost:8080/clientes

{
  "nome": "Maria Oliveira",
  "nomeSocial": "Maria",
  "dataNascimento": "1985-05-15",
  "dataCadastro": "2023-01-01",
  "documentos": [
    {
      "tipo": "CPF",
      "numero": "98765432100"
    }
  ],
  "endereco": {
    "estado": "MG",
    "cidade": "Belo Horizonte",
    "bairro": "Savassi",
    "rua": "Rua da Bahia",
    "numero": "1000",
    "codigoPostal": "30160000"
  },
  "telefones": [
    {
      "ddd": "31",
      "numero": "987654321"
    }
  ]
}
```

Atualizar Cliente
```
PUT http://localhost:8080/clientes
Content-Type: application/json

{
  "id": 1,
  "nome": "Maria Oliveira Santos",
  "nomeSocial": "Mari",
  "dataNascimento": "1985-05-15"
}
```
Excluir Cliente

```
DELETE http://localhost:8080/clientes


{
  "id": 1
}
```
Obter Documentos do Cliente
```
GET http://localhost:8080/clientes/1/documentos
```
Obter Endereço do Cliente
```
GET http://localhost:8080/clientes/1/endereco
```
Obter Telefones do Cliente
```
GET http://localhost:8080/clientes/1/telefones
```

### Documento
Obter Documento por ID
```
GET http://localhost:8080/documento/1
```
Obter Todos os Documentos
```
GET http://localhost:8080/documentos
```
Cadastrar Documento
```
POST http://localhost:8080/documento/cadastro

{
  "tipo": "RG",
  "numero": "112223334"
}
```
Atualizar Documento
```
PUT http://localhost:8080/documento/atualizar

{
  "id": 1,
  "tipo": "CNH",
  "numero": "12345678901"
}
```
Excluir Documento
```
DELETE /documento/excluir

{
  "id": 1
}
```
### Endereco
Obter Endereço por ID
```
GET http://localhost:8080/endereco/1
```
Obter Todos os Endereços
```
GET http://localhost:8080/enderecos
```
Cadastrar Endereço

```
POST http://localhost:8080/endereco/cadastro

{
  "estado": "SP",
  "cidade": "Campinas",
  "bairro": "Cambuí",
  "rua": "Rua José Paulino",
  "numero": "1234",
  "codigoPostal": "13010000"
}
```
Atualizar Endereço
```
PUT http://localhost:8080/endereco/atualizar
Content-Type: application/json

{
  "id": 1,
  "cidade": "Campinas",
  "bairro": "Centro",
  "rua": "Avenida Francisco Glicério",
  "numero": "500"
}
```
Excluir Endereço

DELETE http://localhost:8080/endereco/excluir

{
  "id": 1
}

### Telefone
Obter Telefone por ID
```
GET http://localhost:8080/telefone/1
```
Obter Todos os Telefones
```
GET http://localhost:8080/telefones
```

Cadastrar Telefone
```
POST http://localhost:8080/telefone/cadastro

{
  "ddd": "19",
  "numero": "987654321"
}
```
Atualizar Telefone
```
PUT http://localhost:8080/telefone/atualizar


{
  "id": 1,
  "ddd": "19",
  "numero": "912345678"
}
```
Excluir Telefone
```
DELETE http://localhost:8080/telefone/excluir

{
  "id": 1
}
```

