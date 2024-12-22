package com.example.Desafio_CRUD_DB.service;

import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTests {
    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    @Test
    void testCriarPessoaComSucesso() {

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
    void testCriarPessoaComDadosInvalidos() {
        Pessoa pessoaInvalida = new Pessoa();
        pessoaInvalida.setNome(null);
        pessoaInvalida.setCpf("");

        when(pessoaRepository.save(pessoaInvalida)).thenThrow(new IllegalArgumentException("Dados inválidos"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pessoaService.criarPessoa(pessoaInvalida));
        assertEquals("Dados inválidos", exception.getMessage());
    }

    @Test
    void testCriarPessoaCpfDuplicado() {

        Pessoa pessoa = new Pessoa();
        pessoa.setNome("João Silva");
        pessoa.setCpf("123.456.789-00");

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> pessoaService.criarPessoa(pessoa));

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(pessoaRepository, never()).save(pessoa);
    }

    @Test
    void testBuscarPessoaPeloIdComSucesso() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("Eduarda da Fonseca");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("José de Souza");

        when(pessoaRepository.findById(1L)).thenReturn(java.util.Optional.of(pessoa1));

        Pessoa pessoaEncontrada = pessoaService.buscarPorId(1L);


        assertNotNull(pessoaEncontrada);
        assertEquals(1L, pessoaEncontrada.getId());
        verify(pessoaRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPessoaPeloIdSemSucesso() {
        when(pessoaRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> pessoaService.buscarPorId(1L));

        assertEquals("404 NOT_FOUND \"Pessoa nao encontrada\"", exception.getMessage());
        verify(pessoaRepository, times(1)).findById(1L);
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
    void testAlterarPessoaInexistente() {
        Long idInexistente = 199L;
        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setNome("Abreu");

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> pessoaService.atualizarPessoa(idInexistente, pessoaAtualizada));

        assertEquals("404 NOT_FOUND \"Pessoa nao encontrada\"", exception.getMessage());
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @Test
    void testListarPessoasComSucesso() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("Ednelson");

        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("Jésica");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pessoa> paginarPessoas = new PageImpl<>(List.of(pessoa1, pessoa2), pageable, 2);

        when(pessoaRepository.findAll(pageable)).thenReturn(paginarPessoas);

        Page<Pessoa> pessoas = pessoaService.listarTodas(pageable);

        assertNotNull(pessoas);
        assertEquals(2, pessoas.getTotalElements(), "A lista de pessoas deve conter 2 elementos");
        verify(pessoaRepository, times(1)).findAll(pageable);
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

    @Test
    void testExcluirPessoaInexistente() {
        Long idInexistente = 299L;

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> pessoaService.excluirPessoa(idInexistente));

        assertEquals("404 NOT_FOUND \"Pessoa nao encontrada\"", exception.getMessage());
        verify(pessoaRepository, never()).deleteById(idInexistente);
    }

}