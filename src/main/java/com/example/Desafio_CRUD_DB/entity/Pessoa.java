package com.example.Desafio_CRUD_DB.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    @NotBlank(message = "CPF é obrigatório")
    @Column(unique = true)
    private String cpf;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();

    public int getIdade() {
        return Period.between(this.dataNascimento, LocalDate.now()).getYears();
    }
}
