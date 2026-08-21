package com.ops.oscontrol.usuario;

import com.ops.oscontrol.domain.StatusCadastro;
import com.ops.oscontrol.tenant.Empresa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_email", columnNames = "email"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(name = "senha_hash", nullable = false, length = 100)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PapelUsuario papel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    protected Usuario() {
    }

    public Usuario(Empresa empresa, String nome, String email, String senhaHash, PapelUsuario papel) {
        this.empresa = Objects.requireNonNull(empresa, "Empresa é obrigatória");
        this.nome = validarTexto(nome, "Nome do usuário é obrigatório");
        this.email = validarTexto(email, "E-mail é obrigatório").toLowerCase();
        this.senhaHash = validarTexto(senhaHash, "Senha codificada é obrigatória");
        this.papel = Objects.requireNonNull(papel, "Papel do usuário é obrigatório");
        this.status = StatusCadastro.ATIVO;
    }

    public Long getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public PapelUsuario getPapel() { return papel; }
    public StatusCadastro getStatus() { return status; }

    private static String validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }
}
