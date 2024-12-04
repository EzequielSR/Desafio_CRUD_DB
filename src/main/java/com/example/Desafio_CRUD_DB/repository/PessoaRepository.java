package com.example.Desafio_CRUD_DB.repository;

import com.example.Desafio_CRUD_DB.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    boolean existsByCpf(String cpf);
}