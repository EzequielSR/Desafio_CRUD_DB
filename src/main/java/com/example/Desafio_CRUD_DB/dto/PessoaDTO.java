package com.example.Desafio_CRUD_DB.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PessoaDTO {
    private Long id;
    private String nome;
    private String dataNascimento;
    private String cpf;
    private List<EnderecoDTO> enderecos;
}
