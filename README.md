# API Catálogo de Filmes

API REST desenvolvida com Java e Spring Boot para cadastrar, consultar,
filtrar, atualizar e remover filmes. O projeto utiliza PostgreSQL como
banco de dados, executado em um container Docker, enquanto a aplicação
Spring Boot roda localmente.

## Tecnologias

-   Java 17
-   Spring Boot 4.1.1
-   Spring Web MVC
-   Spring Data JPA / Hibernate
-   Jakarta Bean Validation
-   PostgreSQL 18
-   Docker Compose
-   Maven
-   Lombok
-   JUnit

## Funcionalidades

-   Cadastro de filmes.
-   Consulta de um filme pelo ID.
-   Listagem paginada.
-   Filtros por gênero, avaliação mínima e ano de lançamento.
-   Atualização de filmes.
-   Exclusão de filmes.
-   Validação dos dados recebidos.
-   Resposta padronizada para filmes não encontrados.

## Pré-requisitos

Instale ou tenha disponível:

-   JDK 17.
-   Docker Engine ou Docker Desktop com Docker Compose.
-   Git, caso vá clonar o repositório.
-   Uma ferramenta para enviar requisições HTTP, como Postman, Insomnia
    ou `curl`.

A aplicação é executada localmente; somente o PostgreSQL é iniciado pelo
Docker Compose.

## Configuração

### 1. Configure as variáveis de ambiente

Na raiz do projeto, crie um arquivo `.env` com os valores do ambiente
local:

``` dotenv
POSTGRES_USER=postgres
POSTGRES_PASSWORD=defina-uma-senha-local
POSTGRES_DB=db-test
```

Não versione o arquivo `.env` nem compartilhe credenciais reais.
Mantenha um `.env.example` no repositório com valores ilustrativos, se
necessário.

O Docker Compose usa esse arquivo para substituir as variáveis
declaradas em `docker-compose.yaml`. O Spring Boot não carrega `.env`
automaticamente: configure as mesmas variáveis na execução da aplicação
pelo IntelliJ IDEA (Run → Edit Configurations → Environment variables)
ou disponibilize-as no ambiente do processo Java.

### 2. Inicie o PostgreSQL

Na raiz do projeto, execute:

``` bash
docker compose up -d
```

O serviço publica a porta `5410` da máquina na porta `5432` do
container. O banco será acessível localmente em `localhost:5410`.

Verifique o container:

``` bash
docker compose ps
docker logs postgres-catalogo-db
```

Para encerrar o container sem remover os dados:

``` bash
docker compose down
```

Os dados ficam em um volume Docker nomeado. Para apagar também o volume
e todos os dados persistidos, use `docker compose down -v` --- faça isso
somente se realmente quiser excluir o banco local.

### 3. Configure a aplicação

O arquivo `src/main/resources/application.yaml` utiliza as variáveis de
ambiente e está configurado para a porta `9010`:

``` yaml
spring:
  application:
    name: api-catalogo
  datasource:
    url: jdbc:postgresql://localhost:5410/${POSTGRES_DB}
    driver-class-name: org.postgresql.Driver
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 9010
```

Se executar pelo IntelliJ, selecione a classe principal
`com.viratech.ApiCatalogoApplication` em uma configuração
**Application** ou **Spring Boot** e defina as variáveis
`POSTGRES_USER`, `POSTGRES_PASSWORD` e `POSTGRES_DB`.

Confirme que a URL utiliza a porta `5410`, e não `5432`, pois essa é a
porta publicada pelo Compose neste projeto.

> **Nota sobre o schema:** `spring.jpa.hibernate.ddl-auto=update`
> permite que o Hibernate atualize o schema durante o desenvolvimento.
> Para ambientes de produção, prefira migrações versionadas, por exemplo
> com Flyway ou Liquibase.

### 4. Execute a aplicação

Pelo Maven Wrapper:

``` bash
# Linux/macOS
./mvnw spring-boot:run
```

``` powershell
# Windows
.\mvnw.cmd spring-boot:run
```

Ou execute a classe `ApiCatalogoApplication` pelo IntelliJ IDEA, com as
variáveis de ambiente configuradas.

Quando a aplicação iniciar, a API estará disponível em:

``` text
http://localhost:9010
```

## Endpoints

A base dos endpoints é `/api/movies`.

  ---------------------------------------------------------------------------
  Método            Endpoint             Descrição         Resposta esperada
  ----------------- -------------------- ----------------- ------------------
  `POST`            `/api/movies`        Cadastra um filme `201 Created`

  `GET`             `/api/movies`        Lista filmes com  `200 OK`
                                         paginação e       
                                         filtros           

  `GET`             `/api/movies/{id}`   Consulta filme    `200 OK`
                                         pelo ID           

  `PUT`             `/api/movies/{id}`   Atualiza um filme `202 Accepted`

  `DELETE`          `/api/movies/{id}`   Exclui um filme   `204 No Content`
  ---------------------------------------------------------------------------

### Cadastrar filme

`POST /api/movies`

Exemplo de corpo JSON:

``` json
{
  "title": "Interestelar",
  "description": "Uma equipe viaja pelo espaço em busca de condições para a sobrevivência humana.",
  "genre": "drama",
  "releaseYear": 2014,
  "rating": 8.7
}
```

O campo `genre` aceita os valores `action`, `adventure`, `animation`,
`comedy` e `drama`.

Regras de validação implementadas no DTO:

-   `title`: obrigatório, entre 3 e 100 caracteres.
-   `description`: obrigatória, entre 10 e 300 caracteres.
-   `genre`: obrigatório.
-   `releaseYear`: obrigatório e igual ou superior a 1900.
-   `rating`: obrigatório, entre `0.0` e `10.0`.

### Listar e filtrar filmes

`GET /api/movies`

A listagem aceita os parâmetros de paginação do Spring Data e os filtros
abaixo:

  -----------------------------------------------------------------------
  Parâmetro               Tipo                    Descrição
  ----------------------- ----------------------- -----------------------
  `page`                  inteiro                 Número da página,
                                                  começando em `0`

  `size`                  inteiro                 Quantidade de itens por
                                                  página

  `sort`                  texto                   Campo e direção de
                                                  ordenação, por exemplo
                                                  `title,asc`

  `genre`                 texto                   Filtra pelo gênero

  `minRating`             decimal                 Retorna filmes com
                                                  avaliação maior ou
                                                  igual ao valor
                                                  informado

  `releaseYear`           inteiro                 Filtra pelo ano de
                                                  lançamento
  -----------------------------------------------------------------------

Exemplos:

``` http
GET http://localhost:9010/api/movies?page=0&size=10
GET http://localhost:9010/api/movies?genre=drama&minRating=8
GET http://localhost:9010/api/movies?releaseYear=2014&page=0&size=5
GET http://localhost:9010/api/movies?sort=title,asc
```

A resposta de listagem contém `content`, `totalElements`, `totalPages`,
`currentPage` e `pageSize`.

Exemplo ilustrativo:

``` json
{
  "content": [
    {
      "id": 1,
      "title": "Interestelar",
      "description": "Uma equipe viaja pelo espaço em busca de condições para a sobrevivência humana.",
      "genre": "drama",
      "releaseYear": 2014,
      "rating": 8.7,
      "createdAt": "2026-10-17T12:00:00"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10
}
```

### Consultar por ID

`GET /api/movies/1`

### Atualizar filme

`PUT /api/movies/1`

Envie no corpo os mesmos campos exigidos no cadastro. A operação
atualiza os dados do filme identificado pelo ID.

### Excluir filme

`DELETE /api/movies/1`

Em caso de sucesso, a API retorna `204 No Content`.

### Erros

Quando o filme não é encontrado, a aplicação retorna `404 Not Found` com
uma estrutura semelhante a:

``` json
{
  "codeStatus": 404,
  "message": "Movie not found",
  "timestamp": "2026-10-17T12:00:00",
  "path": "/api/movies/999"
}
```

Os valores de data, ID e caminho são exemplos.

## Estrutura do projeto

``` text
src/
├── main/
│   ├── java/com/viratech/
│   │   ├── controller/      # Endpoints REST
│   │   ├── dto/             # Objetos de entrada e saída
│   │   ├── enums/           # Gêneros de filmes
│   │   ├── exceptions/      # Exceções e tratamento global
│   │   ├── mapper/          # Conversão entre entidade e DTO
│   │   ├── model/           # Entidade JPA
│   │   ├── repository/      # Acesso a dados
│   │   ├── service/         # Regras de negócio
│   │   ├── specification/   # Filtros dinâmicos
│   │   └── ApiCatalogoApplication.java
│   └── resources/
│       └── application.yaml
└── test/
    └── java/com/viratech/
```

## Executar os testes

Execute:

``` bash
# Linux/macOS
./mvnw test
```

``` powershell
# Windows
.\mvnw.cmd test
```

## Possíveis problemas

**Erro de conexão com o PostgreSQL** - Confirme que o container está em
execução com `docker compose ps`. - Confirme que a porta publicada é
`5410:5432`. - Verifique se as três variáveis de ambiente estão
disponíveis para o processo Java. - Confirme que o banco definido em
`POSTGRES_DB` existe na mesma instância à qual a aplicação está
conectando.

**Erro informando que o banco não existe** - Verifique o nome do banco
no `.env`. - Lembre-se de que as variáveis `POSTGRES_DB`,
`POSTGRES_USER` e `POSTGRES_PASSWORD` inicializam o banco quando o
diretório de dados está vazio. Alterá-las depois que o volume foi criado
não recria o banco nem troca automaticamente a senha.

**A porta já está em uso** - Verifique se outro serviço usa a porta
`5410` e altere o mapeamento no Compose e a URL JDBC se necessário.

## Próximas melhorias

-   Adicionar migrações de banco com Flyway.
-   Ampliar a cobertura de testes unitários e de integração.
-   Documentar a API com OpenAPI/Swagger.
-   Padronizar o tratamento de erros de validação.
-   Definir limites e valores padrão para paginação.

## Licença

Este projeto está em desenvolvimento para fins de estudo e portfólio.
Adicione uma licença explícita ao repositório caso deseje permitir
reutilização por terceiros.
