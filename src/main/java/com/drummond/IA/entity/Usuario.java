package com.drummond.IA.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "Nome_Completo", nullable = false , unique = false, length = 100)
    private String nome;
    @Column(name = "password", nullable = false, length = 200)
    private String password;
    @Column(name = "telefone",nullable = false,length = 15)
    private String  telefone;
    @Column(name = "data_Nascimento",nullable = false,length = 10)
    private String dataDeNascimento;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    private Role role = Role.ROLE_ALUNO;

    public enum Role {
        ROLE_ADMIN, ROLE_ALUNO
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                '}';
    }



}
