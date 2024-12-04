package com.example.Desafio_CRUD_DB.service;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscarPorId(Long id) {
        return pessoaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa nao encontrada"));
    }

    @Transactional
    public Pessoa criarPessoa(Pessoa pessoa) {
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        for (Endereco endereco : pessoa.getEnderecos()) {
            endereco.setPessoa(pessoa);
        }
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Pessoa atualizarPessoa(Long id, Pessoa dadosAtualizados) {
        Pessoa pessoaExistente = pessoaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));

        pessoaExistente.setNome(dadosAtualizados.getNome());
        pessoaExistente.setDataNascimento(dadosAtualizados.getDataNascimento());
        pessoaExistente.setCpf(dadosAtualizados.getCpf());

        pessoaExistente.getEnderecos().clear();
        for (Endereco endereco : dadosAtualizados.getEnderecos()) {
            endereco.setPessoa(pessoaExistente);
            pessoaExistente.getEnderecos().add(endereco);
        }
        return pessoaRepository.save(pessoaExistente);
    }

    @Transactional
    public void excluirPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada"));
        Period.between(pessoa.getDataNascimento(), LocalDate.now());
    }

    public int calcularIdade(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa não encontrada."));
        return Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
    }
}
