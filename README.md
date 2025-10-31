# 🚀 Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acesse: http://localhost:8080/health

## 📋 Endpoints Principais

### 🔍 Health & Info
- **GET** `/health` - Status da aplicação (inclui versão Java)
- **GET** `/info` - Informações da aplicação
- **GET** `/h2-console` - Console do banco H2

### 👥 Clientes
- **POST** `/clientes` - Criar cliente
- **GET** `/clientes` - Listar clientes ativos
- **GET** `/clientes/{id}` - Buscar cliente por ID
- **GET** `/clientes/email/{email}` - Buscar por email
- **GET** `/clientes/buscar?nome={nome}` - Buscar por nome
- **PUT** `/clientes/{id}` - Atualizar cliente
- **DELETE** `/clientes/{id}` - Desativar cliente
- **PATCH** `/clientes/{id}/ativar` - Ativar cliente

### 🍽️ Restaurantes
- **POST** `/restaurantes` - Criar restaurante
- **GET** `/restaurantes` - Listar restaurantes ativos
- **GET** `/restaurantes/{id}` - Buscar por ID
- **GET** `/restaurantes/category/{category}` - Buscar por categoria
- **GET** `/restaurantes/search?name={nome}` - Buscar por nome
- **GET** `/restaurantes/search/term?term={termo}` - Buscar por nome ou categoria
- **GET** `/restaurantes/top` - Melhores restaurantes (por avaliação)
- **PUT** `/restaurantes/{id}` - Atualizar restaurante
- **DELETE** `/restaurantes/{id}` - Desativar restaurante
- **PATCH** `/restaurantes/{id}/activate` - Ativar restaurante
- **PATCH** `/restaurantes/{id}/rating` - Atualizar avaliação

### 🍕 Produtos
- **POST** `/produtos/restaurante/{restaurantId}` - Criar produto
- **GET** `/produtos` - Listar produtos disponíveis
- **GET** `/produtos/{id}` - Buscar por ID
- **GET** `/produtos/restaurante/{restaurantId}` - Produtos por restaurante
- **GET** `/produtos/restaurante/{restaurantId}/disponiveis` - Produtos disponíveis por restaurante
- **GET** `/produtos/categoria/{categoria}` - Produtos por categoria
- **GET** `/produtos/restaurante/{restaurantId}/categoria/{categoria}` - Produtos por restaurante e categoria
- **PUT** `/produtos/{id}` - Atualizar produto
- **DELETE** `/produtos/{id}` - Desativar produto
- **PATCH** `/produtos/{id}/ativar` - Ativar produto
- **PATCH** `/produtos/{id}/disponibilidade` - Alterar disponibilidade

### 📦 Pedidos
- **POST** `/pedidos` - Criar pedido
- **GET** `/pedidos` - Listar pedidos pendentes
- **GET** `/pedidos/{id}` - Buscar pedido por ID
- **GET** `/pedidos/client/{clientId}` - Pedidos por cliente
- **GET** `/pedidos/client/{clientId}/active` - Pedidos ativos por cliente
- **GET** `/pedidos/status/{status}` - Pedidos por status
- **PATCH** `/pedidos/{id}/status` - Atualizar status do pedido
- **PATCH** `/pedidos/{id}/cancel` - Cancelar pedido
- **GET** `/pedidos/{id}/estimated-time` - Tempo estimado de entrega

## 🗄️ Status dos Pedidos
- `PENDING` - Pendente
- `CONFIRMED` - Confirmado
- `PREPARING` - Em preparação
- `READY_FOR_DELIVERY` - Pronto para entrega
- `IN_DELIVERY` - Em entrega
- `DELIVERED` - Entregue
- `CANCELLED` - Cancelado

## 💾 Banco de Dados
- **Tipo**: H2 em memória
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:deliverydb`
- **Usuário**: `sa`
- **Senha**: (vazia)

## 🎯 Exemplos de Uso

### Criar Cliente
```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@email.com",
    "phone": "11999999999",
    "address": "Rua A, 123"
  }'