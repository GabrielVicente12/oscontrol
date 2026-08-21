package com.ops.oscontrol.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdemServicoTest {

    @Test
    void deveCriarOrdemServicoAbertaComDadosValidos() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        assertEquals("OS-2026-001", ordem.getNumero());
        assertEquals("Revisão preventiva", ordem.getDescricao());
        assertEquals(StatusOrdemServico.ABERTA, ordem.getStatus());
        assertEquals(LocalDate.of(2026, 8, 20), ordem.getDataAbertura());
    }

    @Test
    void deveCalcularValorPrevisto() {
        BigDecimal valor = novaOrdem("2.50", "120.00").calcularValorPrevisto();
        assertEquals(0, new BigDecimal("300.00").compareTo(valor));
    }

    @Test
    void naoDeveCriarOrdemComNumeroEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> novaOrdem(" ", "Revisão", "2.50", "120.00"));
    }

    @Test
    void naoDeveCriarOrdemComDescricaoEmBranco() {
        assertThrows(IllegalArgumentException.class, () -> novaOrdem("OS-2026-001", " ", "2.50", "120.00"));
    }

    @Test
    void naoDeveCriarOrdemComHorasNegativas() {
        assertThrows(IllegalArgumentException.class, () -> novaOrdem("-0.01", "120.00"));
    }

    @Test
    void naoDeveCriarOrdemComValorHoraNegativo() {
        assertThrows(IllegalArgumentException.class, () -> novaOrdem("2.50", "-0.01"));
    }

    @Test
    void naoDeveCriarOrdemSemDataDeAbertura() {
        assertThrows(NullPointerException.class, () -> new OrdemServico(
                "OS-2026-001", "Revisão", BigDecimal.ONE, BigDecimal.ONE, null));
    }

    @Test
    void deveIniciarEConcluirOrdemServico() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        ordem.iniciar();
        assertEquals(StatusOrdemServico.EM_ANDAMENTO, ordem.getStatus());
        ordem.concluir();
        assertEquals(StatusOrdemServico.CONCLUIDA, ordem.getStatus());
    }

    @Test
    void deveCancelarOrdemServico() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        ordem.cancelar();
        assertEquals(StatusOrdemServico.CANCELADA, ordem.getStatus());
    }

    @Test
    void naoDeveIniciarOrdemCancelada() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        ordem.cancelar();
        assertThrows(IllegalStateException.class, ordem::iniciar);
    }

    @Test
    void naoDeveConcluirOrdemAberta() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        assertThrows(IllegalStateException.class, ordem::concluir);
    }

    @Test
    void deveAlterarDadosPorComportamentosExplicitos() {
        OrdemServico ordem = novaOrdem("2.50", "120.00");
        ordem.alterarDescricao("Troca de correia");
        ordem.alterarHorasPrevistas(new BigDecimal("3.00"));
        ordem.alterarValorHora(new BigDecimal("130.00"));
        assertEquals("Troca de correia", ordem.getDescricao());
        assertEquals(0, new BigDecimal("3.00").compareTo(ordem.getHorasPrevistas()));
        assertEquals(0, new BigDecimal("130.00").compareTo(ordem.getValorHora()));
    }

    private OrdemServico novaOrdem(String horas, String valorHora) {
        return novaOrdem("OS-2026-001", "Revisão preventiva", horas, valorHora);
    }

    private OrdemServico novaOrdem(String numero, String descricao, String horas, String valorHora) {
        return new OrdemServico(numero, descricao, new BigDecimal(horas), new BigDecimal(valorHora),
                LocalDate.of(2026, 8, 20));
    }
}
