package com.drummond.IA.web.controller;

import com.drummond.IA.jwt.JwtToken;
import com.drummond.IA.jwt.JwtUserDetailsService;
import com.drummond.IA.jwt.JwtUtils;
import com.drummond.IA.jwt.RefreshToken;
import com.drummond.IA.web.dto.RefreshTokenDto;
import com.drummond.IA.web.dto.UsuarioLoginDto;
import com.drummond.IA.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken tokenAccess = detailsService.getTokenAuthenticated(dto.getEmail());
            RefreshToken refreshToken = detailsService.getRefreshToken(dto.getEmail());

            return ResponseEntity.ok(Map.of("accessToken",tokenAccess.getToken(),"refreshToken",refreshToken.getToken()));
        } catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username '{}'", dto.getEmail());
        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Inválidas"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();

        try {
            if (JwtUtils.isTokenValid(refreshToken)) {
                String email = JwtUtils.getUsernameFromToken(refreshToken);
                if (email != null) {
                    JwtToken newAccessToken = detailsService.getTokenAuthenticated(email);
                    return ResponseEntity.ok(Map.of("accessToken", newAccessToken.getToken()));
                } else {
                    return ResponseEntity.badRequest().body("Invalid refresh token");
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }
        } catch (AuthenticationException ex) {
            log.error("Error refreshing token: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Error refreshing token");
        }
    }
}
