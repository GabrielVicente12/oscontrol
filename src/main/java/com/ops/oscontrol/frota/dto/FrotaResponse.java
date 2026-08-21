package com.ops.oscontrol.frota.dto;

import com.ops.oscontrol.domain.StatusCadastro;

public record FrotaResponse(Long id, String nome, String descricao, StatusCadastro status) {
}
