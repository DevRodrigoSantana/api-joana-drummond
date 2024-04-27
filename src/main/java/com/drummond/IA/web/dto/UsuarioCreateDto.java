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
public class UsuarioCreateDto {

    @NotBlank
    @Email(message = "formato do e-mail está invalido", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String email;
    @NotBlank
    @Pattern(message = "Formato de senha não contem caracteres validos ",  regexp = "^[a-zA-Z0-9\\s\\p{Punct}]+$")
    @Size(min = 6, max = 15)
    private String password;
    @NotBlank
    @Pattern(message = "Formato de nome Ivalido", regexp = "^[a-zA-Z\\s]+$")
    private String nome;
    @NotBlank
    @Pattern(message = "data de nascimento de formato Incorreto",regexp = "^\\d{2}/\\d{2}/\\d{4}$" )
    private String  dataDeNascimento;
    @NotBlank
    @Pattern(message = "formato de telefone Incorreto",regexp = "^\\(?(\\d{2})\\)?[ ]?(\\d{4,5})[- ]?(\\d{4})$")
    private String telefone;
}

