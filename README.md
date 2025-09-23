# Sistema de Gestão de Projetos e Equipes

## Objetivo do Projeto

Este é um sistema desktop desenvolvido em Java Swing para gerenciamento de projetos e equipes. O sistema permite:

- **Gestão de Usuários**: Cadastro de usuários com diferentes perfis (Administrador, Gerente, Colaborador)
- **Gestão de Projetos**: Criação e acompanhamento de projetos com status, datas e responsáveis
- **Gestão de Equipes**: Formação de equipes, adição de membros e associação com projetos
- **Relatórios**: Visualização de estatísticas e relatórios detalhados do sistema

## Como Executar

### Pré-requisitos

- **Java 19** ou superior instalado
- **Maven** para gerenciamento de dependências

### Passos para Execução

1. **Clone ou baixe o projeto** para sua máquina local

2. **Navegue até a pasta do projeto**:
   ```bash
   cd ProjetoEquipe
   ```

3. **Compile o projeto** usando Maven:
   ```bash
   mvn clean compile
   ```

4. **Execute a aplicação**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.uam.Main"
   ```

   **Ou alternativamente**, compile e execute diretamente:
   ```bash
   mvn clean compile exec:java
   ```

### Executando via IDE

Se preferir usar uma IDE (como IntelliJ IDEA, Eclipse ou VS Code):

1. Importe o projeto como um projeto Maven
2. Aguarde o download das dependências
3. Execute a classe `Main.java` localizada em `src/main/java/com/uam/Main.java`