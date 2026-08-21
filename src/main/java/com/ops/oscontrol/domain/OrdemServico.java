package com.ops.oscontrol.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "ordem_servico",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ordem_servico_numero",
                columnNames = "numero"))
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String numero;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(name = "horas_previstas", nullable = false, precision = 10, scale = 2)
    private BigDecimal horasPrevistas;

    @Column(name = "valor_hora", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorHora;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrdemServico status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "categoria_servico_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_ordem_servico_categoria_servico"))
    private CategoriaServico categoriaServico;

    protected OrdemServico() {
    }

    public OrdemServico(String numero, String descricao, BigDecimal horasPrevistas,
                        BigDecimal valorHora, LocalDate dataAbertura) {
        this.numero = validarTextoObrigatorio(numero, "Número da ordem de serviço é obrigatório");
        this.descricao = validarTextoObrigatorio(descricao, "Descrição é obrigatória");
        this.horasPrevistas = validarNaoNegativo(horasPrevistas, "Horas previstas não podem ser negativas");
        this.valorHora = validarNaoNegativo(valorHora, "Valor por hora não pode ser negativo");
        this.dataAbertura = Objects.requireNonNull(dataAbertura, "Data de abertura é obrigatória");
        this.status = StatusOrdemServico.ABERTA;
    }

    public BigDecimal calcularValorPrevisto() {
        return horasPrevistas.multiply(valorHora).setScale(2, RoundingMode.HALF_UP);
    }

    public void iniciar() {
        if (status != StatusOrdemServico.ABERTA) {
            throw new IllegalStateException("Somente uma ordem aberta pode ser iniciada");
        }
        this.status = StatusOrdemServico.EM_ANDAMENTO;
    }

    public void concluir() {
        if (status != StatusOrdemServico.EM_ANDAMENTO) {
            throw new IllegalStateException("Somente uma ordem em andamento pode ser concluída");
        }
        this.status = StatusOrdemServico.CONCLUIDA;
    }

    public void cancelar() {
        if (status == StatusOrdemServico.CONCLUIDA) {
            throw new IllegalStateException("Uma ordem concluída não pode ser cancelada");
        }
        if (status == StatusOrdemServico.CANCELADA) {
            throw new IllegalStateException("A ordem de serviço já está cancelada");
        }
        this.status = StatusOrdemServico.CANCELADA;
    }

    public void alterarDescricao(String novaDescricao) {
        this.descricao = validarTextoObrigatorio(novaDescricao, "Descrição é obrigatória");
    }

    public void alterarHorasPrevistas(BigDecimal novasHorasPrevistas) {
        this.horasPrevistas = validarNaoNegativo(novasHorasPrevistas, "Horas previstas não podem ser negativas");
    }

    public void alterarValorHora(BigDecimal novoValorHora) {
        this.valorHora = validarNaoNegativo(novoValorHora, "Valor por hora não pode ser negativo");
    }

    void associarA(CategoriaServico categoriaServico) {
        Objects.requireNonNull(categoriaServico, "Categoria de serviço é obrigatória");
        if (this.categoriaServico != null && this.categoriaServico != categoriaServico) {
            throw new IllegalStateException("Ordem de serviço já pertence a outra categoria");
        }
        this.categoriaServico = categoriaServico;
    }

    public String getNumero() { return numero; }
    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public BigDecimal getHorasPrevistas() { return horasPrevistas; }
    public BigDecimal getValorHora() { return valorHora; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public StatusOrdemServico getStatus() { return status; }
    public CategoriaServico getCategoriaServico() { return categoriaServico; }

    private static String validarTextoObrigatorio(String texto, String mensagem) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return texto.trim();
    }

    private static BigDecimal validarNaoNegativo(BigDecimal valor, String mensagem) {
        Objects.requireNonNull(valor, mensagem);
        if (valor.signum() < 0) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }
}
