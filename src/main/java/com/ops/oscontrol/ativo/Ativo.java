package com.ops.oscontrol.ativo;

import com.ops.oscontrol.domain.StatusCadastro;
import com.ops.oscontrol.frota.Frota;
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
@Table(name = "ativo", uniqueConstraints = @UniqueConstraint(
        name = "uk_ativo_empresa_codigo", columnNames = {"empresa_id", "codigo"}))
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frota_id")
    private Frota frota;

    @Column(nullable = false, length = 60)
    private String codigo;

    @Column(nullable = false, length = 180)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAtivo tipo;

    @Column(length = 20)
    private String placa;

    @Column(length = 60)
    private String patrimonio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusCadastro status;

    protected Ativo() {
    }

    public Ativo(Empresa empresa, Frota frota, String codigo, String descricao, TipoAtivo tipo,
                 String placa, String patrimonio) {
        this.empresa = Objects.requireNonNull(empresa, "Empresa é obrigatória");
        validarFrotaDaEmpresa(empresa, frota);
        this.frota = frota;
        this.codigo = validarTexto(codigo, "Código do ativo é obrigatório");
        this.descricao = validarTexto(descricao, "Descrição do ativo é obrigatória");
        this.tipo = Objects.requireNonNull(tipo, "Tipo do ativo é obrigatório");
        this.placa = normalizar(placa);
        this.patrimonio = normalizar(patrimonio);
        this.status = StatusCadastro.ATIVO;
    }

    public Long getId() { return id; }
    public Empresa getEmpresa() { return empresa; }
    public Frota getFrota() { return frota; }
    public String getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public TipoAtivo getTipo() { return tipo; }
    public String getPlaca() { return placa; }
    public String getPatrimonio() { return patrimonio; }
    public StatusCadastro getStatus() { return status; }

    private static void validarFrotaDaEmpresa(Empresa empresa, Frota frota) {
        if (frota != null && frota.getEmpresa() != empresa) {
            throw new IllegalArgumentException("Frota deve pertencer à mesma empresa do ativo");
        }
    }

    private static String validarTexto(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }

    private static String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
