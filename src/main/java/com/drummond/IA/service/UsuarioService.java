package com.drummond.IA.service;

import com.drummond.IA.entity.Usuario;
import com.drummond.IA.exception.EntityNotFoundException;
import com.drummond.IA.exception.PasswordInvalidException;
import com.drummond.IA.exception.StatusInavlido;
import com.drummond.IA.exception.UsernameUniqueViolationException;
import com.drummond.IA.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getEmail()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id))
        );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }

        Usuario user = buscarPorId(id);
        if (!passwordEncoder.matches(senhaAtual, user.getPassword())) {
            throw new PasswordInvalidException("Sua senha não confere.");
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUsername(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario com '%s' não encontrado", email))
        );
    }

    @Transactional(readOnly = true)
    public Usuario.Role buscarRolePorUsername(String username) {
        return usuarioRepository.findRoleByEmail(username);
    }

    public void deletarUsuario(Long id, String password, String confirmaPasword) {

        if(!confirmaPasword.equals(password)){
            throw new PasswordInvalidException("Senhas não sao parecidas");
        }

        Usuario user = buscarPorId(id);
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw  new PasswordInvalidException("Senha Incorreta");
        }
        usuarioRepository.delete(user);
    }

    public Usuario atualizarStatus(Long id, String statusString) throws StatusInavlido {
        Usuario.Status novoStatus;
        try {
            novoStatus = Usuario.Status.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Se a string não corresponder a nenhum valor do enum Status, lance uma exceção StatusInvalido
            throw new StatusInavlido("Status inválido: " + statusString);
        }
        Usuario user = buscarPorId(id);
        user.setStatus(novoStatus);
        return  usuarioRepository.save(user);

       }





    }
