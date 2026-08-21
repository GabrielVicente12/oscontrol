package com.ops.oscontrol.frota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FrotaRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 500) String descricao) {
}
