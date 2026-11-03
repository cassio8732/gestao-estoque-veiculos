# Sistema Automotivo — Gestão de Estoque de Veículos

Repositório: https://github.com/cassio8732/gestao-estoque-veiculos

API REST em **Java 17**, **Spring Boot 3** e **H2** (arquivo local, compatível com sintaxe MySQL) para cadastro, consulta, filtros, atualização e remoção de veículos, marcas e modelos (trabalho de Programação Orientada a Objetos).

## Pré-requisitos

- JDK 17 ou superior
- Maven 3.9+ (ou use `./mvnw` dentro de `backend/`)

Não é necessário Docker nem MySQL instalado: o banco roda embutido via **H2**.

## Como executar

```bash
cd backend
./mvnw spring-boot:run
```

A aplicação sobe na porta **8081** com o perfil `dev` e grava o H2 em `backend/data/estoque.*`. No `dev`, o schema é **recriado** a cada inicialização e os dados de demonstração são carregados de novo (tipos de modelo + veículos de exemplo)., cadastra **sempre** os tipos de modelo SUV, Sedan, Hatch e Picape (se ainda não existirem) e carrega marcas/veículos de demonstração na **primeira** execução (quando ainda não há veículos).

### Interface web (HTML)

Com a API rodando, abra [http://localhost:8081/](http://localhost:8081/) para listar, filtrar, cadastrar, editar e excluir veículos.

### Documentação interativa

- Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) — em **Veículos → GET /api/veiculos** (listar), use **Try it out** para ver os campos de filtro (`marca`, `modelo`, `anoMin`, etc.). Não use o GET `/api/veiculos/{id}` para filtrar.
- OpenAPI JSON: [http://localhost:8081/api-docs](http://localhost:8081/api-docs)

### Console H2 (opcional)

- URL: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
- JDBC URL: `jdbc:h2:file:./data/estoque`
- Usuário: `sa`
- Senha: *(vazio)*

Execute o `./mvnw spring-boot:run` a partir da pasta `backend/` para que o caminho `./data/estoque_dev` aponte para o arquivo correto.

## Endpoints principais

| Recurso | Base path        |
|---------|------------------|
| Marcas  | `/api/marcas`    |
| Modelos | `/api/modelos`   |
| Veículos| `/api/veiculos`  |

### Filtros de veículos (GET `/api/veiculos`)

Parâmetros opcionais: `marca`, `modelo`, `anoMin`, `anoMax`, `precoMin`, `precoMax`, `status` (`DISPONIVEL`, `VENDIDO`, `DESCONTINUADO`).

Exemplo:

```bash
curl "http://localhost:8081/api/veiculos?marca=Toyota&status=DISPONIVEL&precoMax=200000"
```

### Atualização parcial (RF4)

```bash
curl -X POST http://localhost:8081/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{"marca":"Toyota","modelo":"SUV","ano":2023,"cor":"Prata","preco":95000,"quilometragem":15000,"status":"DISPONIVEL"}'

**Modelo** = tipo de carroceria (`SUV`, `Sedan`, `Hatch`, `Picape`). Consulte `GET /api/modelos`. A **marca** é independente (Toyota, Fiat…). Sinônimos aceitos: `ret`/`hatchback` → Hatch, `pickup` → Picape.

### Erro `MARCA_ID` / schema antigo

Isso ocorre quando restou um arquivo H2 do layout antigo. Na pasta `backend`:

```bash
chmod +x scripts/reset-db.sh
./scripts/reset-db.sh
./mvnw spring-boot:run
```

Ou apague manualmente `backend/data/` e suba de novo.

```bash
curl -X PATCH http://localhost:8081/api/veiculos/1 \
  -H "Content-Type: application/json" \
  -d '{"preco": 139000, "quilometragem": 25000, "status": "DISPONIVEL"}'
```
```

## Estrutura do projeto

```
backend/src/main/java/com/estoque/veiculos/
  domain/       # Entidades JPA (herança em EntidadeBase)
  repository/   # Spring Data JPA
  service/      # Regras de negócio e filtros (Specification)
  web/          # Controllers REST e DTOs
  exception/    # Tratamento global de erros
  config/       # Carga de dados em dev
```

## Testes

```bash
cd backend
./mvnw test
```

Os testes usam o perfil `test` com H2 em memória.

## Regras de negócio

- Não é permitido excluir **marca** com modelos cadastrados.
- Não é permitido excluir **modelo** com veículos associados.
- Remoção de veículo é física (DELETE); status `VENDIDO` ou `DESCONTINUADO` registra a situação no estoque.

## Relatório e MySQL no trabalho

O enunciado cita MySQL como opção de modelagem física. O script de referência continua em `backend/src/main/resources/db/migration/V1__init.sql`; em execução local este projeto usa **H2 em arquivo** para simplificar o ambiente (JPA gera/atualiza as tabelas automaticamente).
