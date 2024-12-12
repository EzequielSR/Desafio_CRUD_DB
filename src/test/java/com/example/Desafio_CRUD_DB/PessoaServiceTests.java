package com.example.Desafio_CRUD_DB;

import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import com.example.Desafio_CRUD_DB.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTests {
    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    @Test
    void testCriarPessoaComSucesso(){

        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João da Silva");
        pessoa.setCpf("123.456.789-00");

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

        Pessoa pessoaCriada = pessoaService.criarPessoa(pessoa);

        assertNotNull(pessoaCriada);
        assertEquals("João da Silva", pessoaCriada.getNome());
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void testCriarPessoaCpfDuplicado(){

        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pessoaService.criarPessoa(pessoa));

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(pessoaRepository, never()).save(pessoa);
    }

    @Test
    void testAlterarPessoaComSucesso() {

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(1L);
        pessoaExistente.setNome("João da Silva");
        pessoaExistente.setCpf("123.456.789-00");

        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setId(1L);
        pessoaAtualizada.setNome("João Silva");
        pessoaAtualizada.setCpf("123.456.789-00");

        when(pessoaRepository.findById(1L)).thenReturn(java.util.Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaAtualizada);

        Pessoa pessoaResultado = pessoaService.atualizarPessoa(1L, pessoaAtualizada);

       assertNotNull(pessoaResultado);
        assertEquals("João Silva", pessoaResultado.getNome().trim(), "O nome da pessoa não foi atualizado corretamente");
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
    @Test
    void testExcluirPessoaComSucesso() {

        Long pessoaId = 1L;
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(pessoaId);
        pessoaExistente.setNome("João da Silva");
        pessoaExistente.setCpf("123.456.789-00");

        when(pessoaRepository.findById(pessoaId)).thenReturn(java.util.Optional.of(pessoaExistente));
        doNothing().when(pessoaRepository).deleteById(pessoaId);

        pessoaService.excluirPessoa(pessoaId);

        verify(pessoaRepository, times(1)).deleteById(pessoaId);
    }


}
