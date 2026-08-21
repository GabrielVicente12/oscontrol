package com.ops.oscontrol.frota;

import com.ops.oscontrol.frota.dto.FrotaRequest;
import com.ops.oscontrol.frota.dto.FrotaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/frotas")
public class FrotaController {

    private final FrotaService frotaService;

    public FrotaController(FrotaService frotaService) {
        this.frotaService = frotaService;
    }

    @GetMapping
    public List<FrotaResponse> listar() {
        return frotaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public FrotaResponse criar(@Valid @RequestBody FrotaRequest request) {
        return frotaService.criar(request);
    }
}
