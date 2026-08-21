package com.ops.oscontrol.ativo.dto;

import com.ops.oscontrol.ativo.TipoAtivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtivoRequest(
        Long frotaId,
        @NotBlank @Size(max = 60) String codigo,
        @NotBlank @Size(max = 180) String descricao,
        @NotNull TipoAtivo tipo,
        @Size(max = 20) String placa,
        @Size(max = 60) String patrimonio) {
}
