package com.example.Desafio_CRUD_DB.controller;

import com.example.Desafio_CRUD_DB.dto.PessoaDTO;
import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public Page<PessoaDTO> listarTodas(Pageable pageable) {
        return pessoaService.listarTodas(pageable)
                .map(pessoaService::convertToDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPessoaPeloId(@PathVariable Long id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        return ResponseEntity.ok(pessoaService.convertToDTO(pessoa));
    }

    @GetMapping("/{id}/idade")
    public ResponseEntity<Integer> calcularIdade(@PathVariable Long id) {
        return ResponseEntity.ok(pessoaService.calcularIdade(id));
    }

    @PostMapping
    public ResponseEntity<?> criarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO) {
        try {
            Pessoa pessoa = pessoaService.convertToEntity(pessoaDTO);
            Pessoa novaPessoa = pessoaService.criarPessoa(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.convertToDTO(novaPessoa));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody PessoaDTO dadosAtualizados) {
        try {
            Pessoa pessoaAtualizada = pessoaService.convertToEntity(dadosAtualizados);
            Pessoa pessoa = pessoaService.atualizarPessoa(id, pessoaAtualizada);
            return ResponseEntity.ok(pessoaService.convertToDTO(pessoa));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id) {
        pessoaService.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
            // Coletar todos os erros de campo
            List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            // Retornar uma resposta com status 400 e a lista de mensagens de erro
            return new ResponseEntity<>(Collections.singletonMap("message", errorMessages), HttpStatus.BAD_REQUEST);
        }
    }


}
