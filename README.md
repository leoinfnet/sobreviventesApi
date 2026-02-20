# ☣️ Projeto: Registro de Sobreviventes do Apocalipse

## 📖 Contexto

Após o colapso da civilização, as comunidades remanescentes perceberam
que a maior ameaça não era apenas a escassez de recursos — era a
**desinformação**.

Sem um registro confiável, infectados circulavam livremente entre zonas
seguras, recursos eram mal distribuídos e comunidades entravam em
colapso.

Seu objetivo é construir um **Sistema Oficial de Registro de
Sobreviventes**, utilizando:

-   Spring Boot
-   Spring Data JPA
-   H2 em memória (Postgres)
-   Arquitetura em camadas

Este projeto não é apenas um CRUD.  
Ele deve demonstrar domínio, relacionamentos e organização arquitetural.

------------------------------------------------------------------------

# 🎯 Objetivo do Projeto

Implementar uma API REST com:

## 👤 Entidade Principal: Sobrevivente

Campos obrigatórios:

-   id (Long)
-   nome (String)
-   localizacao (String)
-   infectado (boolean)

Regras:

-   Todo sobrevivente começa como **não infectado**
-   A infecção deve ser marcada por uma ação específica

------------------------------------------------------------------------

# 🔗 Relacionamentos Obrigatórios

## 1️⃣ OneToMany — Sobrevivente → Recurso

Cada sobrevivente pode possuir vários recursos.

### Entidade Recurso

Campos:

-   id (Long)
-   nome (String)
-   quantidade (int)

Regras:

-   Deve pertencer a um sobrevivente
-   Remover da lista deve apagar do banco (orphanRemoval)

------------------------------------------------------------------------

## 2️⃣ ManyToMany — Sobrevivente ↔ Comunidade

Um sobrevivente pode participar de várias comunidades. Uma comunidade
pode ter vários sobreviventes.

### Entidade Comunidade

Campos:

-   id (Long)
-   nome (String)
-   zonaSegura (boolean)

Regra de domínio recomendada:

-   Sobrevivente infectado não pode entrar em comunidade marcada como
    zonaSegura

------------------------------------------------------------------------

# 🌐 Endpoints Obrigatórios

## 👤 Sobrevivente

-   POST /sobreviventes → Registrar sobrevivente
-   PATCH /sobreviventes/{id}/infectado → Marcar como infectado
-   GET /sobreviventes/nao-infectados → Listar não infectados
-   GET /sobreviventes/contagem → Retornar contagem agregada

## 📦 Recursos

-   POST /sobreviventes/{id}/recursos → Adicionar recurso
-   DELETE /sobreviventes/{id}/recursos/{recursoId} → Remover recurso

## 🏘 Comunidades

-   POST /comunidades → Criar comunidade
-   POST /sobreviventes/{id}/comunidades/{comunidadeId} → Associar
    sobrevivente

------------------------------------------------------------------------

# 🏗 Arquitetura Esperada

    api
     ├─ controller
     ├─ dto
    service
    persistence
    domain

Regras:

-   Controller não contém regra de negócio
-   Service contém regras e transações
-   Repository apenas persiste
-   Entidade contém comportamento mínimo de domínio


# 🚀 Extensões Futuras (Opcional)

-   Validações com Bean Validation
-   Paginação
-   Testes automatizados
-   Regra de bloqueio para infectados em zonas seguras

------------------------------------------------------------------------

