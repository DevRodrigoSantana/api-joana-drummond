package com.drummond.IA.web.controller;

import com.drummond.IA.entity.Usuario;
import com.drummond.IA.service.UsuarioService;
import com.drummond.IA.web.dto.UsuarioCreateDto;
import com.drummond.IA.web.dto.UsuarioDelete;
import com.drummond.IA.web.dto.UsuarioResponseDto;
import com.drummond.IA.web.dto.UsuarioSenhaDto;
import com.drummond.IA.web.dto.mapper.UsuarioMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponseDto(user));
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseDto(user));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDelete dto){
        usuarioService.deletarUsuario(id,dto.getPassword(),dto.getConfirmaPasword());
        return  ResponseEntity.noContent().build();
    }


    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListaDto(users));
    }
    // funcões especificas do admin




}
