package com.estoque.controle.controller;

import com.estoque.controle.dto.AutenticacaoDTO;
import com.estoque.controle.dto.LoginResponseDTO;
import com.estoque.controle.dto.RegisterDTO;
import com.estoque.controle.model.usuario.NivelDeUsuario;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.repository.UsuarioRepository;
import com.estoque.controle.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve fazer o login do usuário")
    void deveFazerLoginDoUsuario() throws Exception {
        String json = """
                {
                    "email": "teste@teste.com",
                    "senha": "123456"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Não deve fazer o login do usuário")
    void naoDeveFazerLoginDoUsuario() throws Exception {
        String json = """
                {
                    "email": "teste@teste.com",
                    "senha": "123456789"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("Deve registrar um novo usuário.")
    void deveRegistrarUmNovoUsuario() throws Exception{
        String json = """           
                {
                    "email": "teste1@teste.com",
                    "senha": "123456",
                    "nivelDeUsuario": "ADMIN",
                    "nomeUsuario": "admin"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Não deve registrar um novo usuário.")
    void naoDeveRegistrarUmNovoUsuario() throws Exception{
        String json = """           
                {
                    "email": "teste@teste.com",
                    "senha": "123456",
                    "nivelDeUsuario": "ADMIN",
                    "nomeUsuario": "admin"
                }
                """;

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }
}