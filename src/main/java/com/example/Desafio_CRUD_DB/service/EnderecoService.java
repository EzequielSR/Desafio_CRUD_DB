package com.example.Desafio_CRUD_DB.service;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.EnderecoRepository;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final PessoaRepository pessoaRepository;

    public EnderecoService(EnderecoRepository enderecoRepository,PessoaRepository pessoaRepository) {
        this.enderecoRepository = enderecoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }


    public Endereco buscarEnderecoPorId(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
    }

    @Transactional
    public Endereco criarEndereco(Endereco endereco, Long pessoaId) {
        Pessoa pessoa =  pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
        endereco.setPessoa(pessoa);
        return enderecoRepository.save(endereco);
    }


    @Transactional
    public Endereco atualizarEndereco(Long id, Endereco enderecoAtualizado) {
        Endereco enderecoExistente = buscarEnderecoPorId(id);


        enderecoExistente.setRua(enderecoAtualizado.getRua());
        enderecoExistente.setNumero(enderecoAtualizado.getNumero());
        enderecoExistente.setBairro(enderecoAtualizado.getBairro());
        enderecoExistente.setCidade(enderecoAtualizado.getCidade());
        enderecoExistente.setEstado(enderecoAtualizado.getEstado());
        enderecoExistente.setCep(enderecoAtualizado.getCep());

        return enderecoRepository.save(enderecoExistente);
    }

    @Transactional
    public void deletarEndereco(Long id) {
        Endereco endereco = buscarEnderecoPorId(id);
        enderecoRepository.deleteById(id);
    }
}
