package com.ops.oscontrol.ativo.dto;

import com.ops.oscontrol.ativo.TipoAtivo;
import com.ops.oscontrol.domain.StatusCadastro;

public record AtivoResponse(Long id, Long frotaId, String codigo, String descricao, TipoAtivo tipo,
                            String placa, String patrimonio, StatusCadastro status) {
}
