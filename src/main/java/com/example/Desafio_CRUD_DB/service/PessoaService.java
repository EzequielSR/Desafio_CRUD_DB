package com.example.Desafio_CRUD_DB.service;

import com.example.Desafio_CRUD_DB.dto.PessoaDTO;
import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.repository.PessoaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private ModelMapper modelMapper;

    public PessoaDTO convertToDTO(Pessoa pessoa){
        return modelMapper.map(pessoa, PessoaDTO.class);
    }

    public Pessoa convertToEntity(@Valid PessoaDTO pessoaDTO){
        return modelMapper.map(pessoaDTO, Pessoa.class);
    }

    public Page<Pessoa> listarTodas(Pageable pageable){
        return pessoaRepository.findAll(pageable);
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
    public Pessoa atualizarPessoa(Long id, Pessoa pessoaAtualizada) {
        Pessoa pessoaExistente = buscarPorId(id);

        pessoaExistente.setNome(pessoaAtualizada.getNome().trim());
        pessoaExistente.setDataNascimento(pessoaAtualizada.getDataNascimento());
        pessoaExistente.setCpf(pessoaAtualizada.getCpf());

        pessoaExistente.getEnderecos().clear();
        for (Endereco endereco : pessoaAtualizada.getEnderecos()) {
            endereco.setPessoa(pessoaExistente);
            pessoaExistente.getEnderecos().add(endereco);
        }
        return pessoaRepository.save(pessoaExistente);
    }

    @Transactional
    public void excluirPessoa(Long id) {
        Pessoa pessoa = buscarPorId(id);
        pessoaRepository.deleteById(id);
    }

    public int calcularIdade(Long id) {
        Pessoa pessoa = buscarPorId(id);
        return Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
    }
}
