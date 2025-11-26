# 🏫 Sistema de Gestão Escolar

Um aplicativo Desktop completo desenvolvido em **Java** com interface gráfica **JavaFX**, focado no gerenciamento de entidades escolares (Salas, Professores e Alunos). O sistema utiliza **SQLite** para persistência de dados local e gera relatórios em **PDF**.

Este projeto foi desenvolvido com foco em Orientação a Objetos, padrão MVC e construção de executáveis "Fat JAR" via Maven.

---

## 🚀 Funcionalidades

### 1. Gestão de Salas
* Cadastro, visualização e exclusão de salas.
* Definição de capacidade máxima.
* **Associação:** Visualização do professor responsável e lista de alunos matriculados na sala.

### 2. Gestão de Professores
* CRUD completo (Criar, Ler, Atualizar, Deletar).
* Associação de professores a salas.

### 3. Gestão de Alunos
* Cadastro completo com dados detalhados (Nome, Matrícula, Cor/Raça, Renda, Responsáveis, Endereço).
* **Matrícula:** Associação direta a uma sala.
* **Transferência:** Possibilidade de mover o aluno de uma sala para outra durante a edição.
* Exclusão com verificação de segurança.

### 4. Relatórios e Estatísticas
* Geração de tabelas dinâmicas baseadas em critérios:
    * Por Cor / Raça.
    * Por Faixa de Renda Familiar.
    * Por Turma (Sala).
    * Por Endereço (Agrupamento).
* **Exportação PDF:** Botão para salvar o relatório visualizado em arquivo PDF formatado.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17 (LTS)
* **Interface:** JavaFX 17 (com FXML e CSS)
* **Build System:** Maven
* **Banco de Dados:** SQLite (JDBC)
* **Geração de PDF:** iText 7 Core
* **Dependências Extras:** ControlsFX (para UI aprimorada)

---

## ⚙️ Pré-requisitos

Para compilar e rodar este projeto, você precisa ter instalado:

1.  **JDK 17** ou superior.
2.  **Maven** (ou uma IDE que possua Maven, como IntelliJ IDEA).

---

## 📦 Como Fazer o Build (Gerar Executável)

Este projeto utiliza o plugin `maven-shade-plugin` para criar um **"Fat JAR"** (um único arquivo executável contendo o JavaFX, o driver do SQLite e o iText embutidos).

### Passo 1: Limpar e Empacotar
Abra o terminal na pasta raiz do projeto (onde está o `pom.xml`) e execute:

```bash
mvn clean package
```

---

## 📝 Licença
Este projeto foi desenvolvido para fins educacionais. Sinta-se à vontade para modificar e distribuir.
