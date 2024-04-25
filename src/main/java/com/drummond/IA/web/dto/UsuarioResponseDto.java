package com.drummond.IA.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String username;
    private String role;
    private String telefone;
    private String dataDeNascimento;

}