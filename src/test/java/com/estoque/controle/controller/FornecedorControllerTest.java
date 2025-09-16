package com.estoque.controle.controller;

import com.estoque.controle.repository.FornecedorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FornecedorRepository fornecedorRepository;

    String token;

    private String gerarToken(String json) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return new ObjectMapper()
                .readTree(response)
                .get("token")
                .asText();
    }

    @Nested
    class testeUserAdmin {

        @BeforeEach
        void setUp() throws Exception {
            String loginJson = """
                    {
                      "email": "teste@teste.com",
                      "senha": "123456"
                    }
                    """;

            token = gerarToken(loginJson);

        }

        @Test
        @DisplayName("Deve cadastrar um fornecedor no banco de dados")
        void deveCadastrarNoBancoDeDados() throws Exception {
            String jsonFornecedor = """
                    {
                        "nomeFornecedor": "Joãozinho",
                        "cpfOuCnpj": "78945612300000",
                        "tipoFornecedor": "JURIDICA"
                    }
                    """;

            mockMvc.perform(post("/fornecedor")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonFornecedor)
                            .header("Authorization", "Bearer " + token)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        void deletar() throws Exception {
        }

        @Test
        void atualizar() {
        }

        @Test
        void buscarTodos() {
        }
    }
}