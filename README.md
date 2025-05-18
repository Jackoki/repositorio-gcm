# Cadastro de Clientes - Pessoas

Este guia fornece as instruções necessárias para configurar e executar o aplicativo de Cadastro de Clientes.

## Configuração do Banco de Dados PostgreSQL

O banco de dados pode ser configurado localmente ou utilizando o Docker.

## Requisitos para compilação

Para compilar o projeto, é necessário ter o Postgres instalado na sua máquina, tendo um usuário chamado postgres com a senha postgres e um database chamado localdb
Para realizar a instalação dos arquivos, o usuário precisará ter o Java sdk-20 e o Maven instalado na máquina, que no Windows é instalado seguindo esse seguinte tutorial: 
https://www.youtube.com/watch?v=-ucX5w8Zm8s

### Opção 1: Configuração com Docker

Execute o seguinte comando para iniciar um contêiner Docker com PostgreSQL:

```bash
docker run -it --rm --name myPostgresDb -p 5432:5432 \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=postgres \
    -e POSTGRES_DB=localdb \
    -d postgres
```

### Opção 2: Configuração Local

Se preferir configurar o PostgreSQL localmente, crie um banco de dados chamado `localdb` e configure as credenciais de usuário como `postgres`/`postgres`.

## Executando o Projeto

Antes de executar o projeto, certifique-se de que:

* O arquivo `mvnw` é executável (no Linux).
* A variável de ambiente `JAVA_HOME` está configurada corretamente.

### Verificar Executável do Maven Wrapper (Linux)

Se necessário, torne o arquivo `mvnw` executável:

```bash
chmod +x mvnw
```

### Configurar `JAVA_HOME`

Identifique o caminho onde a JDK está instalada e configure a variável de ambiente:

```cmd
SET JAVA_HOME="C:\Program-Files\Java\jdk-20"
```

### Comandos para Execução

#### No Linux:

Para compilar e iniciar o projeto:

```bash
./mvnw clean package payara-micro:start
```

Para evitar a execução dos testes:

```bash
./mvnw clean package -DskipTests=true
```

#### No Windows:

Para compilar e iniciar o projeto:

```cmd
mvnw.cmd clean package payara-micro:start
```

Para evitar a execução dos testes:

```cmd
mvnw.cmd clean package -DskipTests=true
```

Após iniciado, o projeto estará acessível em [http://localhost:8080](http://localhost:8080).

## Configuração em Produção

Para executar em produção, é necessário configurar as seguintes variáveis de ambiente:

* `DATABASE_URL`: URL de conexão com o banco de dados (ex.: `jdbc:postgresql://127.0.0.1:5432/localdb`)
* `DATABASE_USERNAME`: Nome do usuário do banco de dados (ex.: `postgres`)
* `DATABASE_PASSWORD`: Senha do usuário do banco de dados (ex.: `postgres`)

### Opção 1: Executar Diretamente

Use o Payara Micro ou outro servidor Java EE compatível:

```bash
java -jar <payara-micro> Cadastro-Pessoas.war
```

O arquivo `.war` está localizado no diretório `target` após a compilação.

### Opção 2: Executar com Docker

Construa a imagem Docker e execute o contêiner:

1. Construa a imagem Docker:

   ```bash
   ./mvnw clean package
   docker build -t cadpessoas:v1 .
   ```

2. Execute o contêiner com as variáveis de ambiente configuradas:

   ```bash
   docker run -it --rm \
       -e DATABASE_URL="jdbc:postgresql://<url do banco de dados>" \
       -e DATABASE_USERNAME="<nome do usuario>" \
       -e DATABASE_PASSWORD="<senha do usuario>" \
       -p 8080:8080 cadpessoas:v1
   ```

Exemplo de comando completo:

```bash
docker run -it --rm \
    -e DATABASE_URL="jdbc:postgresql://192.168.0.110:5432/localdb" \
    -e DATABASE_USERNAME="postgres" \
    -e DATABASE_PASSWORD="postgres" \
    -p 8080:8080 cadpessoas:v1
```

Após iniciado, o projeto estará acessível em [http://localhost:8080/Cadastro-Pessoas](http://localhost:8080/Cadastro-Pessoas).

## Exemplo de Configuração para Produção

Para configurar o banco de dados em produção com Docker:

```bash
docker run -it --rm --name ProductPostgresDb -p 5432:5432 \
    -e POSTGRES_USER=postgres \
    -e POSTGRES_PASSWORD=sudodb \
    -e POSTGRES_DB=customerdb \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -v C:/Users/UTFPR/Downloads/dados:/var/lib/postgresql/data \
    -d postgres
```
