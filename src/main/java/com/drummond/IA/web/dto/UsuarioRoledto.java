package com.drummond.IA.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioRoledto
{

    @Size(min = 3,max = 6)
    @Pattern(message = "Formato de role não contem caracteres validos ",  regexp = "^[A-Za-z]+$")
           private String role;


}

