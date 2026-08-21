package com.ops.oscontrol.ativo;

import com.ops.oscontrol.ativo.dto.AtivoRequest;
import com.ops.oscontrol.ativo.dto.AtivoResponse;
import com.ops.oscontrol.frota.Frota;
import com.ops.oscontrol.frota.FrotaRepository;
import com.ops.oscontrol.security.TenantAtual;
import com.ops.oscontrol.tenant.Empresa;
import com.ops.oscontrol.tenant.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtivoService {

    private final AtivoRepository ativoRepository;
    private final FrotaRepository frotaRepository;
    private final EmpresaRepository empresaRepository;
    private final TenantAtual tenantAtual;

    public AtivoService(AtivoRepository ativoRepository, FrotaRepository frotaRepository,
                        EmpresaRepository empresaRepository, TenantAtual tenantAtual) {
        this.ativoRepository = ativoRepository;
        this.frotaRepository = frotaRepository;
        this.empresaRepository = empresaRepository;
        this.tenantAtual = tenantAtual;
    }

    @Transactional
    public AtivoResponse criar(AtivoRequest request) {
        Long empresaId = tenantAtual.empresaId();
        if (ativoRepository.existsByEmpresaIdAndCodigoIgnoreCase(empresaId, request.codigo())) {
            throw new IllegalArgumentException("Código do ativo já cadastrado");
        }
        Empresa empresa = empresaRepository.getReferenceById(empresaId);
        Frota frota = request.frotaId() == null ? null : frotaRepository
                .findByIdAndEmpresaId(request.frotaId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Frota não encontrada"));
        Ativo ativo = new Ativo(empresa, frota, request.codigo(), request.descricao(), request.tipo(),
                request.placa(), request.patrimonio());
        return converter(ativoRepository.save(ativo));
    }

    @Transactional(readOnly = true)
    public List<AtivoResponse> listar() {
        return ativoRepository.findAllByEmpresaIdOrderByDescricao(tenantAtual.empresaId()).stream()
                .map(this::converter)
                .toList();
    }

    private AtivoResponse converter(Ativo ativo) {
        Long frotaId = ativo.getFrota() == null ? null : ativo.getFrota().getId();
        return new AtivoResponse(ativo.getId(), frotaId, ativo.getCodigo(), ativo.getDescricao(), ativo.getTipo(),
                ativo.getPlaca(), ativo.getPatrimonio(), ativo.getStatus());
    }
}
