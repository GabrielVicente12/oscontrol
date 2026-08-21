package com.ops.oscontrol.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoriaServicoTest {

    @Test
    void deveCriarCategoriaAtiva() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        assertEquals("Manutenção preventiva", categoria.getNome());
        assertEquals(StatusCadastro.ATIVO, categoria.getStatus());
    }

    @Test
    void deveAdicionarOrdemServicoEManejarOsDoisLadosDaAssociacao() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        OrdemServico ordem = novaOrdem("OS-2026-001");
        categoria.adicionarOrdemServico(ordem);
        assertEquals(1, categoria.getOrdensServico().size());
        assertSame(ordem, categoria.getOrdensServico().getFirst());
        assertSame(categoria, ordem.getCategoriaServico());
    }

    @Test
    void naoDeveAdicionarOrdemNula() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        assertThrows(NullPointerException.class, () -> categoria.adicionarOrdemServico(null));
    }

    @Test
    void naoDeveAdicionarDuasOrdensComMesmoNumero() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        categoria.adicionarOrdemServico(novaOrdem("OS-2026-001"));
        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
                () -> categoria.adicionarOrdemServico(novaOrdem("OS-2026-001")));
        assertEquals("Número da ordem de serviço já utilizado na categoria", excecao.getMessage());
    }

    @Test
    void naoDeveAssociarOrdemADuasCategorias() {
        CategoriaServico preventiva = new CategoriaServico("Manutenção preventiva");
        CategoriaServico corretiva = new CategoriaServico("Manutenção corretiva");
        OrdemServico ordem = novaOrdem("OS-2026-001");
        preventiva.adicionarOrdemServico(ordem);
        assertThrows(IllegalStateException.class, () -> corretiva.adicionarOrdemServico(ordem));
    }

    @Test
    void naoDeveExporListaInternaModificavel() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        categoria.adicionarOrdemServico(novaOrdem("OS-2026-001"));
        assertThrows(UnsupportedOperationException.class,
                () -> categoria.getOrdensServico().add(novaOrdem("OS-2026-002")));
    }

    @Test
    void deveAlterarStatusPorComportamentoExplicito() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        categoria.inativar();
        assertEquals(StatusCadastro.INATIVO, categoria.getStatus());
        categoria.ativar();
        assertEquals(StatusCadastro.ATIVO, categoria.getStatus());
    }

    private OrdemServico novaOrdem(String numero) {
        return new OrdemServico(numero, "Revisão preventiva", new BigDecimal("2.50"),
                new BigDecimal("120.00"), LocalDate.of(2026, 8, 20));
    }
}
