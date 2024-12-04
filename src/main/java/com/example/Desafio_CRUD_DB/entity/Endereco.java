package com.example.Desafio_CRUD_DB.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @NotBlank(message = "Numero é obrigatória")
    private String numero;

    @NotBlank(message = "Bairro é obrigatória")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatória")
    private String estado;

    @NotBlank(message = "Rua é obrigatória")
    private String cep;

    private final Boolean principal = false;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

}
