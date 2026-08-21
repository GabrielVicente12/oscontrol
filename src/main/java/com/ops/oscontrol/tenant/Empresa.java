package com.ops.oscontrol.tenant;

import com.ops.oscontrol.domain.StatusCadastro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    protected Empresa() {
    }

    public Empresa(String nome) {
        this.nome = validarNome(nome);
        this.status = StatusCadastro.ATIVO;
    }

    public void alterarNome(String novoNome) {
        this.nome = validarNome(novoNome);
    }

    public void ativar() {
        this.status = StatusCadastro.ATIVO;
    }

    public void inativar() {
        this.status = StatusCadastro.INATIVO;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public StatusCadastro getStatus() {
        return status;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório");
        }
        return nome.trim();
    }
}
