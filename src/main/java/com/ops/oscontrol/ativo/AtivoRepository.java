package com.ops.oscontrol.ativo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtivoRepository extends JpaRepository<Ativo, Long> {

    List<Ativo> findAllByEmpresaIdOrderByDescricao(Long empresaId);

    boolean existsByEmpresaIdAndCodigoIgnoreCase(Long empresaId, String codigo);
}
