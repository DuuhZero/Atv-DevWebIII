## Como rodar aplicação atividade 4

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

### 1. Autenticação (Login)
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "nomeUsuario": "lula",
  "senha": "123456"
}
```

Resposta (Token JWT):
```
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWxhIiwiZXhwIjoxNjUwMj..."
}
```

#### **É de muitissima importância que você adicione o valor do token no seu auth bearer, para que execute os próximos comandos** 

### 2. Rotas

#### Usuários
Listar todos usuários (ADMIN, GERENTE)

```
GET http://localhost:8080/usuarios/listar
```

Buscar usuário por ID (ADMIN, GERENTE)
```
GET http://localhost:8080/usuarios/buscar/{id}
```
Criar usuário (ADMIN)
```
POST http://localhost:8080/usuarios/criar
Content-Type: application/json

{
  "nome": "Novo Usuário",
  "credencial": {
    "nomeUsuario": "novo",
    "senha": "123456"
  },
  "perfis": ["ROLE_VENDEDOR"],
  "telefones": [{
    "ddd": "11",
    "numero": "987654321"
  }],
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Centro",
    "rua": "Av. Paulista",
    "numero": "1000",
    "codigoPostal": "01310-100"
  }
}
```
Atualizar usuário (ADMIN)
```

PUT http://localhost:8080/usuarios/{id}
Content-Type: application/json

{
  "nome": "Usuário Atualizado",
  "credencial": {
    "nomeUsuario": "usuario_att"
  },
  "telefones": [{
    "id": 1,
    "ddd": "11",
    "numero": "912345678"
  }],
  "endereco": {
    "id": 1,
    "estado": "RJ",
    "cidade": "Rio de Janeiro",
    "bairro": "Copacabana",
    "rua": "Av. Atlântica",
    "numero": "500",
    "codigoPostal": "22010-000"
  }
}
```
Deletar usuário (ADMIN)
```
DELETE http://localhost:8080/usuarios/deletar/{id}
```
#### Clientes
Listar clientes (ADMIN, GERENTE, VENDEDOR)

```
GET http://localhost:8080/clientes
```

Buscar cliente por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/clientes/{id}
```
Criar cliente (ADMIN, GERENTE, VENDEDOR)
```
POST http://localhost:8080/clientes
Content-Type: application/json

{
  "nome": "Cliente Novo",
  "credencial": {
    "nomeUsuario": "cliente",
    "senha": "123456"
  },
  "perfis": ["ROLE_CLIENTE"],
  "telefones": [{
    "ddd": "11",
    "numero": "912345678"
  }],
  "endereco": {
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Moema",
    "rua": "Av. Ibirapuera",
    "numero": "100",
    "codigoPostal": "04001-000"
  }
}
```
Atualizar cliente (ADMIN, GERENTE)
```

PUT http://localhost:8080/clientes/{id}
Content-Type: application/json

{
  "nome": "Cliente Atualizado",
  "telefones": [{
    "id": 1,
    "ddd": "11",
    "numero": "998877665"
  }],
  "endereco": {
    "id": 1,
    "estado": "SP",
    "cidade": "São Paulo",
    "bairro": "Itaim Bibi",
    "rua": "Rua Joaquim Floriano",
    "numero": "100",
    "complemento": "Apto 201",
    "codigoPostal": "04534-000"
  }
}
```
Deletar cliente (ADMIN, GERENTE)
```
DELETE http://localhost:8080/clientes/{id}
```
Meu cadastro (CLIENTE)
```
GET http://localhost:8080/clientes/meu-cadastro
```
#### Vendedores
Listar vendedores (ADMIN, GERENTE)
```
GET http://localhost:8080/vendedores/listar
```
Criar vendedor (ADMIN, GERENTE)
```
POST http://localhost:8080/vendedores/criar
Content-Type: application/json

{
  "nome": "Vendedor Novo",
  "credencial": {
    "nomeUsuario": "vendedor",
    "senha": "123456"
  },
  "perfis": ["ROLE_VENDEDOR"],
  "email": "vendedor@email.com",
  "telefone": "11987654321"
}
```
Atualizar vendedor (ADMIN, GERENTE)
```
PUT http://localhost:8080/vendedores/{id}
Content-Type: application/json

{
  "nome": "Vendedor Atualizado",
  "email": "novoemail@vendedor.com",
  "telefone": "11999999999"
}
```
Deletar vendedor (ADMIN, GERENTE)
```
DELETE http://localhost:8080/vendedores/deletar/{id}
```
#### Gerentes
Listar gerentes (ADMIN)
```
GET http://localhost:8080/gerentes/listar
```
Criar gerente (ADMIN)
```
POST http://localhost:8080/gerentes/criar
Content-Type: application/json

{
  "nome": "Novo Gerente",
  "credencial": {
    "nomeUsuario": "gerente",
    "senha": "123456"
  },
  "perfis": ["ROLE_GERENTE"],
  "email": "gerente@email.com",
  "telefone": "11987654321"
}
```
Atualizar gerente (ADMIN)
```
PUT http://localhost:8080/gerentes/{id}
Content-Type: application/json

{
  "nome": "Gerente Atualizado",
  "email": "novoemail@gerente.com",
  "telefone": "11999999999"
}
```
Deletar gerente (ADMIN)
```
DELETE http://localhost:8080/gerentes/deletar/{id}
```
#### Mercadorias
Listar mercadorias (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/mercadorias
```
Buscar mercadoria por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/mercadorias/{id}
```
Criar mercadoria (ADMIN, GERENTE)
```
POST http://localhost:8080/mercadorias
Content-Type: application/json

{
  "nome": "Pneu Aro 15",
  "quantidade": 10,
  "valor": 250.0,
  "descricao": "Pneu novo para carros populares"
}
```
Atualizar mercadoria (ADMIN, GERENTE)
```
PUT http://localhost:8080/mercadorias
Content-Type: application/json

{
  "id": 1,
  "nome": "Pneu Aro 15 Atualizado",
  "quantidade": 15,
  "valor": 280.0,
  "descricao": "Pneu premium para carros populares"
}
```
Deletar mercadoria (ADMIN)
```
DELETE http://localhost:8080/mercadorias/{id}
```
#### Serviços
Listar serviços (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/servicos
```
Buscar serviço por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/servicos/{id}
```
Criar serviço (ADMIN, GERENTE)
```
POST http://localhost:8080/servicos
Content-Type: application/json

{
  "nome": "Troca de Óleo",
  "valor": 80.0,
  "descricao": "Troca de óleo completa"
}
```
Atualizar serviço (ADMIN, GERENTE)
```
PUT http://localhost:8080/servicos
Content-Type: application/json

{
  "id": 1,
  "nome": "Troca de Óleo Premium",
  "valor": 100.0,
  "descricao": "Troca de óleo com lubrificação completa"
}
```
Deletar serviço (ADMIN)
```
DELETE http://localhost:8080/servicos/{id}
```
#### Vendas
Listar todas vendas (ADMIN)

```
GET http://localhost:8080/vendas
```
Registrar venda (VENDEDOR, GERENTE)
```
POST http://localhost:8080/vendas/cadastrar
Content-Type: application/json

{
  "cliente": {"id": 1},
  "mercadorias": [{"id": 1}],
  "servicos": [{"id": 1}]
}
```
Atualizar venda (ADMIN, GERENTE)
```
PUT http://localhost:8080/vendas/{id}
Content-Type: application/json

{
  "cliente": {"id": 2},
  "mercadorias": [{"id": 2}],
  "servicos": [{"id": 2}]
}
```
Deletar venda (ADMIN, GERENTE)

```
DELETE http://localhost:8080/vendas/{id}
```
Minhas vendas (VENDEDOR, GERENTE)
```
GET http://localhost:8080/vendas/minhas-vendas
```
Minhas compras (CLIENTE)
```
GET http://localhost:8080/vendas/minhas-compras
```
#### Empresa
Ver dados da empresa (ADMIN, GERENTE)
```
GET http://localhost:8080/empresa
```