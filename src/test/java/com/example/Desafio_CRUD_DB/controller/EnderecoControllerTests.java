package com.example.Desafio_CRUD_DB.controller;

import com.example.Desafio_CRUD_DB.service.EnderecoService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
 class EnderecoControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this.enderecoController);
    }
    @Test
    void deveListarTodosOsEnderecos() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enderecos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void deveCriarNovoEndereco() throws Exception {

        String pessoaJson = """
            {
                "nome": "João Silva",
                "cpf": "123.456.789-00",
                "dataNascimento": "1980-01-01",
                "enderecos": []
            }
            """;

        var pessoaResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String pessoaId = pessoaResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        String enderecoJson = """
            {
                "rua": "Rua A",
                "numero": "123",
                "bairro": "Bairro X",
                "cidade": "Cidade Y",
                "estado": "PA",
                "cep": "99999-999"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/enderecos/{pessoaId}", Long.parseLong(pessoaId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rua").value("Rua A"))
                .andExpect(jsonPath("$.numero").value("123"))
                .andExpect(jsonPath("$.bairro").value("Bairro X"))
                .andExpect(jsonPath("$.cidade").value("Cidade Y"))
                .andExpect(jsonPath("$.estado").value("PA"))
                .andExpect(jsonPath("$.cep").value("99999-999"));
    }
    @Test
    void deveRetornarErroAoCriarEnderecoComDadosInvalidos() throws Exception {

        String pessoaJson = """
            {
                "nome": "João Silva",
                "cpf": "123.456.769-00",
                "dataNascimento": "1980-01-01",
                "enderecos": []
            }
            """;

        var pessoaResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String pessoaId = pessoaResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        String enderecoJson = """
            {
                "rua": "Rua A",
                "numero": "123",
                "bairro": "Bairro X",
                "cidade": "Cidade Y",
                "estado": "Roraima",
                "cep": "00000-000"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/enderecos/{pessoaId}", Long.parseLong(pessoaId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message[0]").value("O estado deve ter exatamente 2 caracteres."));
    }

    @Test
    void deveRetornarEnderecoPeloId() throws Exception {

        String pessoaJson = """
            {
                "nome": "João Silva",
                "cpf": "123.456.989-00",
                "dataNascimento": "1980-01-01",
                "enderecos": []
            }
            """;

        var pessoaResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String pessoaId = pessoaResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        String enderecoJson = """
            {
                "rua": "Rua A",
                "numero": "123",
                "bairro": "Bairro X",
                "cidade": "Cidade Y",
                "estado": "SP",
                "cep": "44444-444"
            }
            """;

        var enderecoResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/enderecos/{pessoaId}", Long.parseLong(pessoaId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoJson))
                .andExpect(status().isCreated())
                .andReturn();

        String enderecoId = enderecoResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/enderecos/{id}", Long.parseLong(enderecoId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rua").value("Rua A"))
                .andExpect(jsonPath("$.numero").value("123"))
                .andExpect(jsonPath("$.bairro").value("Bairro X"))
                .andExpect(jsonPath("$.cidade").value("Cidade Y"))
                .andExpect(jsonPath("$.estado").value("SP"))
                .andExpect(jsonPath("$.cep").value("44444-444"));
    }
    @Test
    void deveRetornarErroAoBuscarEnderecoInexistente() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/enderecos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirEndereco() throws Exception {

        String pessoaJson = """
            {
                "nome": "João Silva",
                "cpf": "123.456.389-00",
                "dataNascimento": "1980-01-01",
                "enderecos": []
            }
            """;

        var pessoaResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/pessoas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pessoaJson))
                .andExpect(status().isCreated())
                .andReturn();

        String pessoaId = pessoaResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        String enderecoJson = """
            {
                "rua": "Rua A",
                "numero": "143",
                "bairro": "Bairro X",
                "cidade": "Cidade Y",
                "estado": "SC",
                "cep": "88888-888"
            }
            """;

        var enderecoResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/enderecos/{pessoaId}", Long.parseLong(pessoaId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enderecoJson))
                .andExpect(status().isCreated())
                .andReturn();

        String enderecoId = enderecoResponse.getResponse().getContentAsString().split(",")[0].split(":")[1].trim().replace("\"", "");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/enderecos/{id}", Long.parseLong(enderecoId)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarErroAoExcluirEnderecoInexistente() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/enderecos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

}
