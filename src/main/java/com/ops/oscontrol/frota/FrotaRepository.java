package com.ops.oscontrol.frota;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FrotaRepository extends JpaRepository<Frota, Long> {

    List<Frota> findAllByEmpresaIdOrderByNome(Long empresaId);

    Optional<Frota> findByIdAndEmpresaId(Long id, Long empresaId);
}
