# DanilloVidalLopes-Framework-Java-Automation
Automacao Basica de java com BDD para chrome e edge

# Framework de Automação de Testes Web

Este repositório contém um framework robusto para automação de testes de aplicações web, construído com Java e focado em manutenibilidade, escalabilidade e clareza.

## Sobre o Projeto

O principal objetivo deste framework é fornecer uma estrutura sólida que permita a criação de testes automatizados de forma rápida e eficiente. Ele combina as melhores práticas de mercado para garantir que os testes sejam fáceis de escrever, ler e manter, ao mesmo tempo que gera evidências de execução detalhadas em formato PDF.

## Tecnologias Utilizadas

*   **Linguagem:** [Java](https://www.java.com/) (JDK 23+)
*   **Automação Web:** [Selenium WebDriver](https://www.selenium.dev/)
*   **Desenvolvimento Orientado a Comportamento (BDD):** [Cucumber](https://cucumber.io/)
*   **Executor de Testes:** [JUnit 5](https://junit.org/junit5/)
*   **Gerenciador de Dependências e Build:** [Maven](https://maven.apache.org/)
*   **Geração de Evidências em PDF:** [iTextPDF](https://itextpdf.com/)
*   **Relatórios (Opcional):** [Allure Framework](https://qameta.io/allure-framework/)

## Conceitos de Arquitetura

O framework foi projetado sobre três pilares principais:

### 1. BDD (Behavior-Driven Development)
Utilizamos o Cucumber para escrever cenários de teste em linguagem natural (Gherkin), dentro de arquivos `.feature`. Isso melhora a comunicação entre desenvolvedores, QAs e stakeholders de negócio, garantindo que os testes reflitam os requisitos reais da aplicação.

### 2. Page Object Model (POM) & Programação Orientada a Objetos (OOP)
Cada página da aplicação web é representada por uma classe Java (ex: `GooglePage.java`). Esta classe é responsável por:
*   Mapear os elementos da interface (botões, campos de texto, etc.) através de localizadores (`By`).
*   Encapsular os métodos que representam as ações do usuário naquela página (ex: `search(String text)`).

Essa abordagem reduz drasticamente a duplicação de código e torna a manutenção dos testes muito mais simples. Se um elemento da interface mudar, a alteração só precisa ser feita em um único lugar.

### 3. Domain-Driven Design (DDD)
Aplicamos conceitos de DDD ao modelar as páginas como objetos de domínio. Cada "Page" representa uma parte do domínio da aplicação sob teste, com seus próprios comportamentos (métodos) e estado (elementos). Isso cria um código mais expressivo e alinhado com as regras de negócio da aplicação.

## Estrutura do Projeto

```
.
├── src
│   ├── main/java/...         (Código de suporte, se houver)
│   └── test
│       ├── java
│       │   └── com/automation/framework
│       │       ├── infrastructure    (Drivers, Listeners, Wrappers)
│       │       ├── presentation      (Pages, Steps)
│       │       └── runners           (Classes para executar os testes)
│       └── resources
│           ├── features            (Arquivos .feature com os cenários BDD)
│           └── junit-platform.properties (Configurações do executor de testes)
├── target/                     (Diretório gerado pelo Maven)
│   ├── allure-results/         (Resultados do Allure)
│   └── evidencias-pdf/         (Relatórios em PDF gerados a cada execução)
├── pom.xml                     (Arquivo de configuração do Maven)
└── README.md                   (Esta documentação)
```

## Começando

Siga os passos abaixo para configurar e executar o projeto em sua máquina local.

### Pré-requisitos

*   **JDK (Java Development Kit)**: Versão 21 ou superior.
*   **Maven**: Versão 3.8 ou superior.
*   **IDE**: IntelliJ IDEA ou Eclipse (recomendado IntelliJ).

### Instalação

1.  Clone o repositório para sua máquina local:
  ```sh
  git clone <URL_DO_SEU_REPOSITÓRIO>
  ```
2.  Navegue até o diretório do projeto:
  ```sh
  cd <NOME_DO_PROJETO>
  ```
3.  Execute o Maven para baixar todas as dependências:
  ```sh
  mvn clean install
  ```

## Como Executar os Testes

### Executando pela IDE (IntelliJ - Método Recomendado)

1.  Abra o projeto no IntelliJ.
2.  Localize a classe `RunCucumberTest.java` no diretório `src/test/java/.../runners`.
3.  Clique com o botão direito do mouse sobre o nome da classe e selecione **"Run 'RunCucumberTest'"**.

> **Importante:** Sempre execute os testes a partir da classe `RunCucumberTest` para garantir que o motor do JUnit 5 seja carregado corretamente, lendo as configurações do arquivo `junit-platform.properties` e ativando o plugin de evidências.

### Executando pela Linha de Comando (Maven)

Você pode executar todos os testes usando o Maven. Navegue até o diretório raiz do projeto e execute:

```sh
mvn test
```

## Filtrando Testes e Configurações

É possível customizar a execução para rodar apenas cenários específifcos ou em navegadores diferentes.

### Filtrando por Tags

1.  Adicione uma tag (ex: `@regressao`) acima de um `Scenario:` ou `Feature:` em seus arquivos `.feature`.
2.  Para executar apenas essa tag:
*   **Via IDE:** Em "Run/Debug Configurations" -> `RunCucumberTest` -> "VM options", adicione:
    ```
    -Dcucumber.filter.tags="@regressao"
    ```
*   **Via Linha de Comando:**
    ```sh
    mvn test -Dcucumber.filter.tags="@regressao"
    ```

### Trocando o Navegador

O navegador padrão é o Chrome. Para executar em outro navegador (ex: Edge):

*   **Via IDE:** Em "Run/Debug Configurations" -> `RunCucumberTest` -> "VM options", adicione:
```
-Dbrowser=edge
```
*   **Via Linha de Comando:**
```sh
mvn test -Dbrowser=edge
`

---