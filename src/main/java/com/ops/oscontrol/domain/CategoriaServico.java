package com.ops.oscontrol.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria_servico")
public class CategoriaServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    @OneToMany(mappedBy = "categoriaServico", fetch = FetchType.LAZY)
    private List<OrdemServico> ordensServico = new ArrayList<>();

    protected CategoriaServico() {
    }

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

    public Long getId() {
        return id;
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
