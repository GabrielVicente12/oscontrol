package com.ops.oscontrol.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class TenantAtual {

    public Long empresaId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Jwt jwt)) {
            throw new IllegalStateException("Usuário autenticado não possui contexto de empresa");
        }
        return Long.valueOf(jwt.getClaimAsString("empresa_id"));
    }
}
