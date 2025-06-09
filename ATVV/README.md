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

### 1. Autenticação (Login)
```
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "nomeUsuario": "admin",
  "senha": "admin"
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


#### REST API (novo da atividade 5)

Listar todas as vendas de serviços ou peças por determinado periodo (periodo utilizado no exemplo ja esta adicionado no run do app)
```
GET http://localhost:8080/api/vendas/empresa/1/periodo?inicio=2025-06-09&fim=2025-06-10
```

Listar todos os clientes cadastrados por empresa
```
GET http://localhost:8080/api/clientes/empresa/1
```

Listar todos os funcionarios cadastrados por empresa
```
GET http://localhost:8080/api/funcionarios/empresa/1
```

Lista de serviços e mercadorias disponiveis por empresa
```
GET http://localhost:8080/api/produtos/empresa/1
```

Lista de todos os veiculos atendidos por empresa
```
GET http://localhost:8080/api/veiculos/empresa/1
```

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
POST http://localhost:8080/usuarios/cadastrar
Content-Type: application/json

{
  "nome": "Novo Usuário",
  "credencial": {
    "nomeUsuario": "novo",
    "senha": "123456"
  },
  "perfis": ["ROLE_VENDEDOR"],
}
```

Deletar usuário (ADMIN)
```
DELETE http://localhost:8080/usuarios/deletar/{id}
```
#### Clientes
Listar clientes (ADMIN, GERENTE, VENDEDOR)

```
GET http://localhost:8080/clientes/listar
```

Buscar cliente por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/clientes/buscar/{id}
```
Criar cliente (ADMIN, GERENTE, VENDEDOR)
```
POST http://localhost:8080/clientes/cadastrar
Content-Type: application/json

{
  "nome": "Cliente Novo",
  "credencial": {
    "nomeUsuario": "cliente",
    "senha": "123456"
  },
  "perfis": ["ROLE_CLIENTE"],
}
```
Atualizar Cliente (ADMIN, GERENTE)
```
PUT http://localhost:8080/clientes/atualizar/{id}
{
  "nome": "Cliente Novo nome"
}
```
Deletar cliente (ADMIN, GERENTE)
```
DELETE http://localhost:8080/clientes/deletar/{id}
```

#### Vendedores
Listar vendedores (ADMIN, GERENTE)
```
GET http://localhost:8080/vendedores/listar
```
Criar vendedor (ADMIN, GERENTE)
```
POST http://localhost:8080/vendedores/cadastrar
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
PUT http://localhost:8080/vendedores/atualizar/{id}
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
POST http://localhost:8080/gerentes/cadastrar
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
Atualizar Gerente (ADMIN)
```
PUT http://localhost:8080/gerentes/atualizar/{id}
{
  "nome": "Novo nome de Gerente "
}
```
Deletar gerente (ADMIN)
```
DELETE http://localhost:8080/gerentes/deletar/{id}
```
#### Mercadorias
Listar mercadorias (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/mercadorias/listar
```
Buscar mercadoria por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/mercadorias/buscar/{id}
```
Criar mercadoria (ADMIN, GERENTE)
```
POST http://localhost:8080/mercadorias/cadastrar
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
PUT http://localhost:8080/mercadorias/atualizar/{id}
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
DELETE http://localhost:8080/mercadorias/deletar/{id}
```
#### Serviços
Listar serviços (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/servicos/listar
```
Buscar serviço por ID (ADMIN, GERENTE, VENDEDOR)
```
GET http://localhost:8080/servicos/buscar/{id}
```
Criar serviço (ADMIN, GERENTE)
```
POST http://localhost:8080/servicos/cadastrar
Content-Type: application/json

{
  "nome": "Troca de Óleo",
  "valor": 80.0,
  "descricao": "Troca de óleo completa"
}
```
Atualizar serviço (ADMIN, GERENTE)
```
PUT http://localhost:8080/servicos/atualizar/{id}
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
DELETE http://localhost:8080/servicos/deletar/{id}
```
#### Vendas
Listar todas vendas (ADMIN, GERENTE)

```
GET http://localhost:8080/vendas/listar
```
Registrar venda (ADMIN,VENDEDOR, GERENTE)
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
PUT http://localhost:8080/vendas/atualizar/{id}
Content-Type: application/json

{
  "cliente": {"id": 2},
  "mercadorias": [{"id": 2}],
  "servicos": [{"id": 2}]
}
```
Deletar venda (ADMIN, GERENTE)

```
DELETE http://localhost:8080/vendas/deletar/{id}
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
Ver dados de todas empresas (ADMIN)
```
GET http://localhost:8080/empresa/listar
```

Buscar dados de uma empresa específica (ADMIN)
```
GET http://localhost:8080/empresa/buscar/{id}
```

Criar uma empresa (ADMIN)
```
POST http://localhost:8080/empresa/cadastrar

{
    "razaoSocial": "Gerson Peças Ltda",
    "nomeFantasia": "Gerson Peças",
    "endereco": {
        "estado": "SP",
        "cidade": "São Paulo",
        "bairro": "Centro",
        "rua": "Rua das Peças",
        "numero": "123",
        "codigoPostal": "01001000"
    },
    "telefones": [
        {
            "ddd": "11",
            "numero": "987654321"
        }
    ],
    "usuarios": [
        {
            "nome": "Gerente da Loja",
            "perfis": ["GERENTE"],
            "credencial": {
                "nomeUsuario": "gerente",
                "senha": "gerente123"
            }
        },
        {
            "nome": "Cliente VIP",
            "perfis": ["CLIENTE"],
            "credencial": {
                "nomeUsuario": "cliente",
                "senha": "cliente123"
            }
        }
    ]
}
```

Atualizar uma empresa (ADMIN)
```
PUT http://localhost:8080/empresa/atualizar/{id}

{
    "nomeFantasia": "Gerson Peças Automotivas",
    "endereco": {
        "numero": "123A"
    }
}
```
Deletar uma empresa (ADMIN)
```
DELETE http://localhost:8080/empresa/deletar/{id}
```
Associar um usuário a tal empresa
```
POST http://localhost:8080/empresa/associar/{idEmpresa}

{
    "idUsuario": 1
}
```

