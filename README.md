# Otimização de Estoque no Varejo: Desenvolvimento de um Sistema Web para Cálculo Dinâmico de Ponto de Pedido e Estoque de Segurança

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

## 🎓 Sobre o Projeto

Este repositório contém o código-fonte do **Trabalho de Graduação (TG)** desenvolvido para o curso de **Informática para Negócios** na **FATEC São José do Rio Preto**.

O projeto consiste em um **Micro SaaS (Software as a Service)** focado na gestão inteligente de estoques. Diferente de sistemas tradicionais que apenas registram entradas e saídas, esta solução implementa algoritmos matemáticos para calcular dinamicamente o **Ponto de Pedido (Reorder Point)** e o **Estoque de Segurança**, baseando-se no desvio padrão da demanda histórica e no Lead Time dos fornecedores.

---

## 🚀 Objetivo

Resolver o problema da **ruptura de estoque** e do **excesso de armazenamento** em pequenas e médias empresas do varejo. O sistema automatiza a decisão de compra, respondendo a perguntas críticas como:
* *"Quanto eu preciso ter de segurança para não perder vendas?"*
* *"Qual é o momento exato de disparar um novo pedido ao fornecedor?"*

---

## 📂 Organização do Código (Arquitetura)

O backend foi construído utilizando **Java 21** e **Spring Boot 4.0.2**, seguindo os princípios da **Arquitetura em Camadas (Layered Architecture)** e **SOLID**. A estrutura de pastas reflete a separação de responsabilidades exigida em projetos de engenharia de software robustos:

### `src/main/java/com/tg/estoque_saas`

* **🕹️ `/controller` (API REST)**
    * Responsável por expor os endpoints (URLs) do sistema.
    * Recebe as requisições HTTP (GET, POST), valida os dados de entrada e responde ao cliente (Frontend/Postman).
    * *Ex:* `VendaController`, `ProdutoController`.

* **🧠 `/service` (Regras de Negócio & Core Matemático)**
    * O "cérebro" do sistema. É aqui que a mágica acontece.
    * Contém a lógica de validação, os cálculos de **Média Ponderada**, **Desvio Padrão** e a decisão de movimentação de estoque.
    * Gerencia a diferença entre **Vendas Históricas** (para estatística) e **Vendas em Tempo Real** (baixa de inventário).

* **📦 `/dto` (Data Transfer Objects)**
    * Objetos criados para transportar dados entre o cliente e o servidor de forma segura, desacoplando a API do banco de dados.
    * *Ex:* `VendaRequest` (permite enviar a flag `vendaAntiga` sem sujar a entidade principal).

* **🗄️ `/repository` (Camada de Dados)**
    * Interfaces que utilizam o **Spring Data JPA** para comunicar com o banco de dados **MySQL**.
    * Abstrai toda a complexidade do SQL, permitindo operações de busca, salvamento e deleção de forma ágil.

* **📑 `/entity` (Modelo de Domínio)**
    * Representação das tabelas do banco de dados em classes Java (ORM - Hibernate).
    * *Ex:* `Produto` (com atributos como `estoqueSeguranca`, `leadTime`) e `Venda`.

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java 21 LTS
* **Framework:** Spring Boot 4.0.2
* **Gerenciamento de Dependências:** Maven
* **Banco de Dados:** MySQL 8.0
* **Testes de API:** Postman

---

## 📈 Status do Desenvolvimento

- [x] Arquitetura Base (Spring Boot + JPA)
- [x] CRUD de Produtos
- [x] Registro de Vendas (Lógica de Histórico vs. Tempo Real)
- [ ] Implementação do Cálculo de Desvio Padrão ($\sigma$)
- [ ] Implementação da Fórmula de Ponto de Pedido ($PP = (Dm \times L) + ES$)
- [ ] Dashboard de Alertas

---

### Autor

**Lucas Grabalos de Souza**
*Estudante de Informática para Negócios - FATEC Rio Preto*