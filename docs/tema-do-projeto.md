# Tema do projeto

O OSControl é uma API didática para gestão de ordens de serviço classificadas por categoria de serviço.

## Estrutura inicial do domínio

| Elemento | Definição |
|---|---|
| Entidade de classificação | `CategoriaServico` |
| Entidade principal | `OrdemServico` |
| Código único | Número da ordem de serviço |
| Medida quantitativa | Horas previstas |
| Valor monetário | Valor por hora |
| Data relevante | Data de abertura |
| Cálculo | Horas previstas multiplicadas pelo valor por hora |

Frotas, ativos e histórico de manutenção fazem parte da evolução planejada, após a conclusão da modelagem e da persistência iniciais.
