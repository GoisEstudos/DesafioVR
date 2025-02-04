# Desafio VR

Este projeto é uma solução para o Desafio VR, desenvolvida em Java utilizando o Gradle como sistema de build e Docker para conteinerização.

## Requisitos
:white_check_mark: Persistência de Dados  
:white_check_mark: Versão do Java: 21  
:white_large_square: Interface Gráfica  
:white_check_mark: Versionamento de Código  
:white_check_mark: Consultas SQL  
:white_large_square: Cobertura de Testes Unitários  
:white_check_mark: Princípios de Design (SOLID)  
:white_check_mark: Arquitetura Limpa (Clean Architecture)  
:white_check_mark: Gerenciamento de Dependências 

## Observações
- A interface gráfica e testes unitarios não foram implementadas devido a limitações de tempo.

## Estrutura do Projeto

- **src/**: Contém o código-fonte do projeto.
- **dados_postgres/**: Possivelmente contém dados ou scripts relacionados ao PostgreSQL.
- **build.gradle**: Script de build do Gradle.
- **docker-compose.yml**: Arquivo de configuração para orquestração de contêineres Docker.

## Pré-requisitos

- [Java 11+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Gradle 6+](https://gradle.org/install/)
- [Docker](https://docs.docker.com/get-docker/)

## Como Executar

1. **Clonar o repositório:**

   ```bash
   git clone https://github.com/GoisEstudos/DesafioVR.git
   cd DesafioVR

2. **Construir o projeto:**

   ```bash
   gradle build

3. **Executar com Docker:**

Certifique-se de que o Docker está instalado e em execução. Utilize o docker-compose para iniciar os serviços:

   ```bash
   docker-compose up -d
   ```

Isso irá configurar e iniciar os contêineres definidos no docker-compose.yml.

4. **Acessar a Documentação da API**

   Após iniciar a aplicação, a documentação da API estará disponível no seguinte endereço:

   [Swagger UI](http://localhost:8080/swagger-ui/index.html)
