package com.example.Desafio_CRUD_DB.service;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.EnderecoRepository;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTests {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @Test
    void testCriarEnderecoComSucesso() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);

        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setPessoa(pessoa);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        Endereco enderecoCriado = enderecoService.criarEndereco(endereco, 1L);

        assertNotNull(enderecoCriado);
        assertEquals("Rua A", enderecoCriado.getRua());
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void testCriarEnderecoParaPessoaInexistente() {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua B");

        when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> enderecoService.criarEndereco(endereco, 99L));

        assertEquals("404 NOT_FOUND \"Pessoa não encontrada\"", exception.getMessage());
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }

    @Test
    void testAtualizarEnderecoComSucesso() {
        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(1L);
        enderecoExistente.setRua("Rua Antiga");

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoExistente));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(enderecoAtualizado);

        Endereco resultado = enderecoService.atualizarEndereco(1L, enderecoAtualizado);

        assertNotNull(resultado);
        assertEquals("Rua Nova", resultado.getRua());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    void testAtualizarEnderecoInexistente() {
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Nova");

        when(enderecoRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> enderecoService.atualizarEndereco(99L, enderecoAtualizado));

        assertEquals("404 NOT_FOUND \"Endereço não encontrado\"", exception.getMessage());
        verify(enderecoRepository, never()).save(any(Endereco.class));
    }

    @Test
    void testBuscarEnderecoPorIdComSucesso() {
        Endereco endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua C");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        Endereco resultado = enderecoService.buscarEnderecoPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarEnderecoPorIdInexistente() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> enderecoService.buscarEnderecoPorId(1L));

        assertEquals("404 NOT_FOUND \"Endereço não encontrado\"", exception.getMessage());
        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void testDeletarEnderecoComSucesso() {
        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));
        doNothing().when(enderecoRepository).deleteById(1L);

        enderecoService.deletarEndereco(1L);

        verify(enderecoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletarEnderecoInexistente() {
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> enderecoService.deletarEndereco(1L));

        assertEquals("404 NOT_FOUND \"Endereço não encontrado\"", exception.getMessage());
        verify(enderecoRepository, never()).deleteById(anyLong());
    }

    @Test
    void testListarTodosEnderecosComSucesso() {
        Endereco endereco1 = new Endereco();
        endereco1.setId(1L);
        endereco1.setRua("Rua A");

        Endereco endereco2 = new Endereco();
        endereco2.setId(2L);
        endereco2.setRua("Rua B");

        when(enderecoRepository.findAll()).thenReturn(List.of(endereco1, endereco2));

        List<Endereco> enderecos = enderecoService.listarTodos();

        assertNotNull(enderecos);
        assertEquals(2, enderecos.size());
        verify(enderecoRepository, times(1)).findAll();
    }
}
