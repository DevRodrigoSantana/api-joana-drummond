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
public class UsuarioDelete {

    @NotBlank
    @Pattern(message = "Formato de senha não contem caracteres validos ",  regexp = "^[a-zA-Z0-9\\s\\p{Punct}]+$")
    @Size(min = 6, max = 15)
    private  String password;
    @NotBlank
    @Pattern(message = "Formato de senha não contem caracteres validos ",  regexp = "^[a-zA-Z0-9\\s\\p{Punct}]+$")
    @Size(min = 6, max = 15)
    private String confirmaPasword;

}
