package com.example.Desafio_CRUD_DB.controller;

import com.example.Desafio_CRUD_DB.dto.EnderecoDTO;
import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarEnderecos() {
        List<EnderecoDTO> enderecosDTO = enderecoService.listarTodos()
                .stream().map(enderecoService::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(enderecosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> buscarEnderecoPeloId(@PathVariable Long id) {
        Endereco endereco = enderecoService.buscarEnderecoPorId(id);
        return ResponseEntity.ok(enderecoService.convertToDTO(endereco));
    }

    @PostMapping("/{pessoaId}")
    public ResponseEntity<?> criarEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO, @Valid @PathVariable Long pessoaId) {
        try {
            Endereco endereco = enderecoService.convertToEntity(enderecoDTO);
            Endereco novoEndereco = enderecoService.criarEndereco(endereco, pessoaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.convertToDTO(novoEndereco));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> atualizarEndereco(@PathVariable Long id, @Valid @RequestBody EnderecoDTO dadosAtualizados) {
        Endereco enderecoAtualizado = enderecoService.atualizarEndereco(id, enderecoService.convertToEntity(dadosAtualizados));
        return ResponseEntity.ok(enderecoService.convertToDTO(enderecoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        enderecoService.deletarEndereco(id);
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
