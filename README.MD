# Ficha de definição do projeto

## Tema do projeto

Sistema SaaS para gestão de ordens de serviço, frotas, máquinas, veículos e equipamentos, voltado ao controle de atendimentos, manutenções e histórico operacional de ativos pertencentes a empresas prestadoras de serviço, frotistas, locadoras, empresas do agronegócio, construção civil e operações de manutenção.

O sistema permitirá cadastrar e organizar frotas, ativos e ordens de serviço, mantendo o histórico de manutenção de cada veículo, máquina ou equipamento e permitindo o acompanhamento de categorias, status, valores, horas previstas e responsáveis pelos atendimentos.

## Identificação

- **Nome do projeto:** oscontrol
- **Tema:** Gestão de Ordens de Serviço, Frotas e Ativos
- **Tipo de aplicação:** SaaS — Software as a Service
- **Tecnologia principal:** Java com Spring Boot
- **Arquitetura inicial:** API REST
- **Banco de dados previsto:** PostgreSQL
- **Objetivo em uma frase:** Permitir o cadastro, acompanhamento e gerenciamento de ordens de serviço vinculadas a frotas, veículos, máquinas e equipamentos, mantendo o histórico de manutenção de cada ativo.

## Problema que o projeto pretende resolver

Empresas que possuem veículos, máquinas ou equipamentos frequentemente realizam o controle de manutenção por meio de planilhas, anotações, mensagens ou sistemas pouco integrados.

Esse modelo dificulta:

- O controle das ordens de serviço.
- O acompanhamento dos ativos da empresa.
- A identificação de equipamentos em manutenção.
- O histórico de serviços executados.
- O acompanhamento de custos de manutenção.
- O controle de frotas.
- A identificação de manutenções preventivas e corretivas.
- A consulta de informações de veículos, máquinas e equipamentos.

O OSControl busca centralizar essas informações em uma plataforma simples, organizada e acessível por meio de uma API REST.

---

## Entidade de classificação

- **Nome no singular:** CategoriaServico
- **Nome no plural:** CategoriasServico
- **Descrição:** Representa a classificação utilizada para organizar os diferentes tipos de serviços e manutenções executados.
- **Exemplo 1:** Manutenção Preventiva
- **Exemplo 2:** Locação

### Exemplos adicionais

- Instalação
- Inspeção
- Reparo
- Revisão
- Suporte Técnico
- Manutenção Elétrica
- Manutenção Mecânica
- Manutenção Hidráulica

---

## Entidade principal

- **Nome no singular:** OrdemServico
- **Nome no plural:** OrdensServico
- **Código único:** Número da ordem de serviço
- **Descrição:** Descrição do serviço solicitado ou problema relatado
- **Medida quantitativa:** Quantidade de horas previstas para execução
- **Valor monetário:** Valor total do serviço
- **Data relevante:** Data de abertura
- **Status:** Aberta, Em Andamento, Concluída ou Cancelada

---

## Entidade Frota

- **Nome no singular:** Frota
- **Nome no plural:** Frotas
- **Descrição:** Representa um agrupamento de veículos, máquinas, implementos ou equipamentos pertencentes a uma empresa.

### Atributos iniciais

- `id`
- `nome`
- `descricao`
- `ativo`
- `dataCadastro`

### Exemplo

```json
{
  "id": 1,
  "nome": "Frota Agrícola",
  "descricao": "Máquinas e veículos utilizados nas operações agrícolas.",
  "ativo": true
}
```
