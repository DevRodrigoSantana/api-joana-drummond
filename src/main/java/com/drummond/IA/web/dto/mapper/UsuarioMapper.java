package com.drummond.IA.web.dto.mapper;

import com.drummond.IA.entity.Usuario;
import com.drummond.IA.web.dto.UsuarioCreateDto;
import com.drummond.IA.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDto createDto){
        return new ModelMapper().map(createDto,Usuario.class);
    }
    public static UsuarioResponseDto toResponseDto( Usuario usuario){
        String role = usuario.getRole().name().substring("ROLE_".length());


        PropertyMap<Usuario,UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario,UsuarioResponseDto.class);
    }
    public static List<UsuarioResponseDto> toListaDto(List<Usuario> usuarios){
        return  usuarios.stream().map(user -> toResponseDto(user)).collect(Collectors.toList());

    }



}
