package com.example.Desafio_CRUD_DB.repository;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
