package com.example.Desafio_CRUD_DB.controller;

import com.example.Desafio_CRUD_DB.entity.Endereco;
import com.example.Desafio_CRUD_DB.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listarEnderecos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    @PostMapping("/{pessoaId}")
    public ResponseEntity<?> criarEndereco(@RequestBody Endereco endereco, @PathVariable Long pessoaId) {
        try {
            Endereco novoEndereco = enderecoService.criarEndereco(endereco, pessoaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoEndereco);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco dadosAtualizados) {
        return ResponseEntity.ok(enderecoService.atualizarEndereco(id, dadosAtualizados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        enderecoService.deletarEndereco(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> buscarEnderecoPeloId(@PathVariable Long id) {
        return ResponseEntity.ok(enderecoService.buscarEnderecoPorId(id));
    }

}
