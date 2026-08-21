package com.ops.oscontrol.persistence;

import com.ops.oscontrol.domain.CategoriaServico;
import com.ops.oscontrol.domain.OrdemServico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class PersistenciaJpaTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void devePersistirERelerCategoriaEOrdemServico() {
        CategoriaServico categoria = new CategoriaServico("Manutenção preventiva");
        OrdemServico ordem = novaOrdem("OS-JPA-001", "Revisão geral");
        categoria.adicionarOrdemServico(ordem);

        entityManager.persist(categoria);
        entityManager.persist(ordem);
        entityManager.flush();

        Long ordemId = ordem.getId();
        entityManager.clear();

        OrdemServico recuperada = entityManager.find(OrdemServico.class, ordemId);
        assertEquals("Manutenção preventiva", recuperada.getCategoriaServico().getNome());
        assertEquals("OS-JPA-001", recuperada.getNumero());
    }

    @Test
    @Transactional
    void deveRegistrarDozeChangeSetsDoLiquibase() {
        Number quantidade = (Number) entityManager.createNativeQuery(
                        "SELECT COUNT(*) FROM databasechangelog")
                .getSingleResult();

        assertEquals(12L, quantidade.longValue());
    }

    @Test
    @Transactional
    void naoDevePersistirNumeroDeOrdemDuplicado() {
        CategoriaServico preventiva = new CategoriaServico("Preventiva");
        CategoriaServico corretiva = new CategoriaServico("Corretiva");
        OrdemServico primeira = novaOrdem("OS-JPA-DUP", "Primeira ordem");
        OrdemServico segunda = novaOrdem("OS-JPA-DUP", "Segunda ordem");
        preventiva.adicionarOrdemServico(primeira);
        corretiva.adicionarOrdemServico(segunda);

        entityManager.persist(preventiva);
        entityManager.persist(corretiva);
        entityManager.persist(primeira);

        assertThrows(PersistenceException.class, () -> entityManager.persist(segunda));
    }

    @Test
    @Transactional
    void naoDeveAceitarHorasPrevistasNegativasNoBanco() {
        CategoriaServico categoria = new CategoriaServico("Inspeção");
        entityManager.persist(categoria);
        entityManager.flush();

        assertThrows(PersistenceException.class, () -> entityManager.createNativeQuery("""
                        INSERT INTO ordem_servico
                            (numero, descricao, horas_previstas, valor_hora, data_abertura, status, categoria_servico_id)
                        VALUES
                            ('OS-JPA-NEG', 'Teste de constraint', -1, 100, CURRENT_DATE, 'ABERTA', :categoriaId)
                        """)
                .setParameter("categoriaId", categoria.getId())
                .executeUpdate());
    }

    private OrdemServico novaOrdem(String numero, String descricao) {
        return new OrdemServico(
                numero,
                descricao,
                new BigDecimal("2.50"),
                new BigDecimal("120.00"),
                LocalDate.of(2026, 8, 20));
    }
}
