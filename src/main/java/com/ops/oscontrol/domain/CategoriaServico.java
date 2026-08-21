package com.ops.oscontrol.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoriaServico {

    private final String nome;
    private StatusCadastro status;
    private final List<OrdemServico> ordensServico = new ArrayList<>();

    public CategoriaServico(String nome) {
        this.nome = validarTextoObrigatorio(nome, "Nome da categoria é obrigatório");
        this.status = StatusCadastro.ATIVO;
    }

    public void adicionarOrdemServico(OrdemServico ordemServico) {
        Objects.requireNonNull(ordemServico, "Ordem de serviço é obrigatória");

        boolean numeroJaUtilizado = ordensServico.stream()
                .anyMatch(ordem -> ordem != ordemServico
                        && ordem.getNumero().equals(ordemServico.getNumero()));

        if (numeroJaUtilizado) {
            throw new IllegalArgumentException("Número da ordem de serviço já utilizado na categoria");
        }

        ordemServico.associarA(this);

        if (!ordensServico.contains(ordemServico)) {
            ordensServico.add(ordemServico);
        }
    }

    public void ativar() {
        this.status = StatusCadastro.ATIVO;
    }

    public void inativar() {
        this.status = StatusCadastro.INATIVO;
    }

    public String getNome() {
        return nome;
    }

    public StatusCadastro getStatus() {
        return status;
    }

    public List<OrdemServico> getOrdensServico() {
        return List.copyOf(ordensServico);
    }

    private static String validarTextoObrigatorio(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }
}
