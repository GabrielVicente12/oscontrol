package com.ops.oscontrol.api;

import java.time.Instant;
import java.util.Map;

public record ErroApi(
        Instant timestamp,
        int status,
        String erro,
        String mensagem,
        Map<String, String> campos) {
}
