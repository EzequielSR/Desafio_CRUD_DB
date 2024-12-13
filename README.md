# Desafio CRUD - Pessoa e Endereço 🚀

Este repositório contém um desafio de implementação de um sistema CRUD em Java com Spring Boot. O sistema permite o gerenciamento de **Pessoas** e **Endereços**, onde cada Pessoa pode ter vários Endereços, criando um relacionamento de um-para-muitos.

Anexo 📎: [CRUD - Pessoa 13.pdf](https://github.com/user-attachments/files/18112284/CRUD.-.Pessoa.13.1.pdf)

---

## Requisitos 📋

- **Java 17** ou superior ☕
- **Spring Boot** (versão 2.x ou superior) 🚀
- **Banco de dados H2** para persistência 🗄️
- **Spring Data JPA** para integração com o banco de dados 🔗
- **Validações básicas** ✔️ e **tratamento de exceções** ⚠️ implementados
- **API RESTful** com respostas em formato **JSON** 🌐

---

## Funcionalidades 🛠️

### CRUD Pessoa
- **Listar todas as pessoas e seus respectivos endereços** 📝
- **Criar uma nova pessoa** com um ou mais endereços 🆕
- **Atualizar os dados de uma pessoa** e/ou seu(s) endereço(s) ✏️
- **Excluir uma pessoa** e todos os seus endereços 🗑️
- **Mostrar a idade da pessoa** 🎂

### CRUD Endereço
- **Listar, criar, atualizar e excluir endereços associados a uma pessoa** 📍
- **Indicar qual endereço é o principal** de uma pessoa 🏠

---

## Funcionalidades Opcionais (Diferenciais) 🌟
- **Swagger** para documentação da API 📑
- **Paginação** na listagem de pessoas 📑🔢
- **Testes de integração** utilizando Spring Boot Test 🧪

---

## Tecnologias Utilizadas 💻
- Java 17: Linguagem de programação utilizada ☕
- Spring Boot: Framework para desenvolvimento de aplicações Java 🚀
- Spring Data JPA: Biblioteca para integração com o banco de dados 🔗
- H2 Database: Banco de dados em memória para persistência de dados 🗄️
- JUnit 5: Framework para testes unitários 🧪
- Swagger (opcional): Ferramenta para documentação da API 📑

---

## Estrutura do Projeto 🏗️

A estrutura do projeto está organizada da seguinte forma:
```plaintext
src/main/java/com/db/Desafio_CRUD_DB
├── controller
│   ├── PessoaController.java
│  
├── entity
│   ├── Pessoa.java
│   └── Endereco.java
├── repository
│   ├── PessoaRepository.java
│   └── EnderecoRepository.java
├── service
│   ├── PessoaService.java
│  
├── resources
└── application.properties
```
---

## Instruções para Rodar o Projeto 🔧

1. **Clone o repositório** 🧑‍💻
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio

2. **Instale as dependências 🔄** <br>
    O projeto utiliza o Maven para gerenciamento de dependências. Para instalar as dependências, execute:
   ```bash
   mvn clean install
   ```

3. **Rodar o projeto ▶️** <br>
   Para rodar o projeto localmente, execute o seguinte comando:
   ```bash
   mvn spring-boot:run
   ```
   
4. **Acessar a API 🌍**  <br>
   A API estará disponível em **http://localhost:8080**. Você pode testar os endpoints da seguinte forma:
- Listar todas as pessoas: **GET /pessoas 📋**
- Criar nova pessoa: **POST /pessoas 🆕**
- Atualizar pessoa: **PUT /pessoas/{id} ✏️**
- Excluir pessoa: **DELETE /pessoas/{id} 🗑️**

5. **Acessar Swagger 📜** <br>
   Se o Swagger foi implementado, acesse a documentação da API no seguinte link:
   ```bash
   http://localhost:8080/swagger-ui/index.html#/
   ```
