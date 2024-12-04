package com.example.Desafio_CRUD_DB.controller;

import com.example.Desafio_CRUD_DB.entity.Pessoa;
import com.example.Desafio_CRUD_DB.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService){
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<List<Pessoa>> listarTodas(){
        return ResponseEntity.ok(pessoaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa){
        return ResponseEntity.ok(pessoaService.criarPessoa(pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id){
        pessoaService.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/idade")
    public ResponseEntity<Integer> calcularIdade(@PathVariable Long id){
        return ResponseEntity.ok(pessoaService.calcularIdade(id));
    }
}
