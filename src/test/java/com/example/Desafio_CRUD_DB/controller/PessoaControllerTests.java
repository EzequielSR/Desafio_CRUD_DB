package com.example.Desafio_CRUD_DB.controller;


import com.example.Desafio_CRUD_DB.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PessoaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PessoaService pessoaService;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this.pessoaController);
    }

    @Test
    void deveCriarNovaPessoa() throws Exception {
        String novaPessoaJson = """
                {
                    "nome": "Maria Oliveira",
                    "cpf": "123.456.779-00",
                    "dataNascimento": "1990-01-01",
                    "enderecos":[]
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(novaPessoaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"))
                .andExpect(jsonPath("$.cpf").value("123.456.779-00"));
    }

    @Test
    void deveRetornarErroAoCriarPessoaComDadosInvalidos() throws Exception {
        String pessoaInvalidaJson = """
                {
                    "nome": "Maria Oliveira",
                    "cpf": "123.456.789-00",
                    "dataNascimento": "01-01",
                    "enderecos":[]
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaInvalidaJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("Data de nascimento inválida"));
    }

    @Test
    void deveListarTodasAsPessoas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pessoas")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



    @Test
    void deveRetornarPessoaPeloId() throws Exception {

        String pessoaJson = """
                {
                    "nome": "João Silva",
                    "cpf": "123.456.789-00",
                    "dataNascimento": "1980-01-01",
                    "enderecos": []
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pessoas/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.dataNascimento").value("1980-01-01"))
                .andExpect(jsonPath("$.enderecos").isEmpty());
    }

    @Test
    void deveRetornarErroAoBuscarPessoaInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/pessoas/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void deveExcluirPessoa() throws Exception {
        String pessoaJson = """
                {
                    "nome": "Maria Oliveira",
                    "cpf": "123.456.789-00",
                    "dataNascimento": "1985-01-01",
                    "enderecos": []
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated());

        doNothing().when(pessoaService).excluirPessoa(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/pessoas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarErroAoExcluirPessoaInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/pessoas/999"))
                .andExpect(status().isNotFound());
    }

}
