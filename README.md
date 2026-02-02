# AvaliacaoINF008

## Padrões de Projeto Adicionados

### 1) Strategy — Questão I

**Problema:** `Autenticador` com múltiplos `if/else` para gerar números de protocolo.

**Solução:** Desacoplar regras em estratégias (`StrategyAutenticacao`), permitindo adicionar novas sem alterar o código.

**Classes:**
- `Autenticador` (Context) — usa a estratégia
- `StrategyAutenticacao` (interface)
- `StrategyAutenticacaoSimples`, `StrategyAutenticacaoPorOrgao`, `StrategyAutenticacaoPorUnidade` (Concrete Strategies)
- `SingletonFactoryStrategyAutenticacao` — registra estratégias dinamicamente

---

### 2) Command — Questão II

**Problema:** Necessidade de log, undo/redo, operações compostas e consolidação.

**Solução:** Encapsular operações como objetos com `execute()`, `undo()`, `redo()`.

**Classes:**
- `Command` (interface) — `execute()`, `undo()`, `redo()`
- `AlterarConteudoCommand`, `AssinarCommand`, `ProtegeCommand`, `TornarUrgenteCommand` (Concrete Commands)
- `CommandInvoker` — executa, desfaz, refaz. Mantém pilhas undo/redo
- `OperationLogger` — registra operações em `logs/doc_<numero>.log`

---

### 3) Composite — Questão II (Macros)

**Problema:** Operações compostas precisam ser executadas como uma unidade.

**Solução:** Agrupar comandos simples em `CompositeCommand` respeitando a interface `Command`.

---

## Padrões originais do projeto (não modificados)

- **Abstract Factory:** `DocumentOperatorFactory` para criação de Documento/Operador
- **Prototype:** `Prototipavel` para clonar documentos/operadores
- **Decorator:** `AssinaturaDecorator`, `SeloUrgenciaDecorator` para adicionar responsabilidades
- **Proxy:** `DocumentoConfidencial` para controlar acesso
