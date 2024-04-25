package com.drummond.IA.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioLoginDto {
    @NotBlank
    @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;
    @NotBlank
    @Pattern(message = "Formato de senha não contem caracteres validos ",  regexp = "^[a-zA-Z0-9\\s\\p{Punct}]+$")
    @Size(min = 6, max = 15)
    private String password;

}
