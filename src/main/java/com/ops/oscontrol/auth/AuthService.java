package com.ops.oscontrol.auth;

import com.ops.oscontrol.auth.dto.LoginRequest;
import com.ops.oscontrol.auth.dto.RegistroRequest;
import com.ops.oscontrol.auth.dto.TokenResponse;
import com.ops.oscontrol.domain.StatusCadastro;
import com.ops.oscontrol.tenant.Empresa;
import com.ops.oscontrol.tenant.EmpresaRepository;
import com.ops.oscontrol.usuario.PapelUsuario;
import com.ops.oscontrol.usuario.Usuario;
import com.ops.oscontrol.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationMinutes;

    public AuthService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder,
                       @Value("${security.jwt.issuer}") String issuer,
                       @Value("${security.jwt.expiration-minutes}") long expirationMinutes) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    @Transactional
    public TokenResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        Empresa empresa = empresaRepository.save(new Empresa(request.empresa()));
        Usuario usuario = usuarioRepository.save(new Usuario(empresa, request.nome(), request.email(),
                passwordEncoder.encode(request.senha()), PapelUsuario.ADMIN));
        return emitir(usuario);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));
        if (usuario.getStatus() != StatusCadastro.ATIVO
                || !passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }
        return emitir(usuario);
    }

    private TokenResponse emitir(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiracao = agora.plus(expirationMinutes, ChronoUnit.MINUTES);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(agora)
                .expiresAt(expiracao)
                .subject(usuario.getId().toString())
                .claim("empresa_id", usuario.getEmpresa().getId().toString())
                .claim("email", usuario.getEmail())
                .claim("papeis", List.of(usuario.getPapel().name()))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", expirationMinutes * 60);
    }
}
