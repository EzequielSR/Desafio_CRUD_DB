package com.example.Desafio_CRUD_DB.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoDTO {
    private Long id;
    private String rua;
    private int numero;
    private String bairro;
    private String cidade;
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres.")
    private String estado;
    private String cep;
    private boolean principal;
}
