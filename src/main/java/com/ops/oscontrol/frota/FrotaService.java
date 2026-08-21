package com.ops.oscontrol.frota;

import com.ops.oscontrol.frota.dto.FrotaRequest;
import com.ops.oscontrol.frota.dto.FrotaResponse;
import com.ops.oscontrol.security.TenantAtual;
import com.ops.oscontrol.tenant.Empresa;
import com.ops.oscontrol.tenant.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FrotaService {

    private final FrotaRepository frotaRepository;
    private final EmpresaRepository empresaRepository;
    private final TenantAtual tenantAtual;

    public FrotaService(FrotaRepository frotaRepository, EmpresaRepository empresaRepository, TenantAtual tenantAtual) {
        this.frotaRepository = frotaRepository;
        this.empresaRepository = empresaRepository;
        this.tenantAtual = tenantAtual;
    }

    @Transactional
    public FrotaResponse criar(FrotaRequest request) {
        Empresa empresa = empresaRepository.getReferenceById(tenantAtual.empresaId());
        return converter(frotaRepository.save(new Frota(empresa, request.nome(), request.descricao())));
    }

    @Transactional(readOnly = true)
    public List<FrotaResponse> listar() {
        return frotaRepository.findAllByEmpresaIdOrderByNome(tenantAtual.empresaId()).stream()
                .map(this::converter)
                .toList();
    }

    private FrotaResponse converter(Frota frota) {
        return new FrotaResponse(frota.getId(), frota.getNome(), frota.getDescricao(), frota.getStatus());
    }
}
