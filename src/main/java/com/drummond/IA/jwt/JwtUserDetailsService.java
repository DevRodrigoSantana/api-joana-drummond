package com.drummond.IA.jwt;

import com.drummond.IA.entity.Usuario;
import com.drummond.IA.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(email);
        return new JwtUserDetails(usuario);
    }

    public JwtToken getTokenAuthenticated(String email) {
        Usuario.Role role = usuarioService.buscarRolePorUsername(email);
        Usuario user = usuarioService.buscarPorUsername(email);
        String id = String.valueOf(user.getId());
        return JwtUtils.createToken(id,email, role.name().substring("ROLE_".length()));
    }
}
