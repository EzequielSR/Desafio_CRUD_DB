package com.example.Desafio_CRUD_DB;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PessoaRepositoryTests {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Test
    void testSalvarPessoaNoBanco() {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Carlos Silva");
        pessoa.setCpf("987.654.321-00");
        pessoa.setDataNascimento(LocalDate.of(1990, 5, 15));

        Endereco endereco1 = new Endereco();
        endereco1.setRua("Rua das Flores");
        endereco1.setNumero("123");
        endereco1.setBairro("Centro");
        endereco1.setCidade("São Paulo");
        endereco1.setEstado("SP");
        endereco1.setCep("01000-000");
        endereco1.setPessoa(pessoa); // Relacionar endereço com a pessoa

        Endereco endereco2 = new Endereco();
        endereco2.setRua("Avenida Paulista");
        endereco2.setNumero("456");
        endereco2.setBairro("Bela Vista");
        endereco2.setCidade("São Paulo");
        endereco2.setEstado("SP");
        endereco2.setCep("01310-000");
        endereco2.setPessoa(pessoa);

        pessoa.setEnderecos(Arrays.asList(endereco1, endereco2));

        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        assertNotNull(pessoaSalva.getId());
        assertEquals("Carlos Silva", pessoaSalva.getNome());
    }

}
