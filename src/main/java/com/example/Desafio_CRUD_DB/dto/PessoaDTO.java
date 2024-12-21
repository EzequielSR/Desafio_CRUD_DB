package com.example.Desafio_CRUD_DB.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PessoaDTO {
    private Long id;
    private String nome;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data de nascimento inválida")
    private String dataNascimento;
    private String cpf;
    private List<EnderecoDTO> enderecos;
}
