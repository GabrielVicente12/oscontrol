# Persistência inicial

A estrutura inicial mantém a relação obrigatória entre `CategoriaServico` e `OrdemServico`, com PostgreSQL e Liquibase como fonte de verdade do schema.

## Integridade do banco

| Restrição | Justificativa |
|---|---|
| `PRIMARY KEY` | Fornece identidade persistente estável para cada categoria e ordem de serviço. |
| `NOT NULL` | Impede registros incompletos para os atributos obrigatórios definidos pelo domínio. |
| `UNIQUE ordem_servico.numero` | Garante que o número identifique uma única ordem mesmo quando a gravação não passa pelas regras em memória. |
| `FOREIGN KEY ordem_servico.categoria_servico_id` | Garante que toda ordem persistida pertença a uma categoria existente. |
| `ON DELETE RESTRICT` | Impede a exclusão de uma categoria enquanto houver ordens vinculadas. |
| `CHECK categoria_servico.status` | Limita o cadastro aos estados `ATIVO` e `INATIVO`. |
| `CHECK ordem_servico.status` | Limita a ordem aos estados previstos em seu ciclo de vida. |
| `CHECK horas_previstas >= 0` | Impede horas negativas independentemente da origem da gravação. |
| `CHECK valor_hora >= 0` | Impede valores por hora negativos independentemente da origem da gravação. |

As validações permanecem no Java para proteger o objeto e produzir erros próximos à operação. As constraints complementam essa proteção para qualquer cliente do banco.

## Configuração local

Os profiles `dev` e `test` usam bancos separados. As URLs, o usuário e as senhas devem ser fornecidos por variáveis de ambiente ou por um arquivo `.env` local criado a partir de `.env.example`.

O arquivo `.env` não é versionado. O profile `prod` recebe todas as credenciais diretamente do ambiente de implantação.
