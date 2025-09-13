package com.estoque.controle.services;

import com.estoque.controle.model.usuario.NivelDeUsuario;
import com.estoque.controle.model.usuario.Usuario;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "segredo123");
    }

    @Test
    void deveGerarUmTokenValido() {
        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");

        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String validaToken = tokenService.validacaoToken(token);
        assertEquals(usuario.getUsername(), validaToken);
    }

    @Test
    void deveRetornarVazioQuandoTokenInvalido() {
        String tokenFake = "abc.def.ghi";
        String resultado = tokenService.validacaoToken(tokenFake);

        assertEquals("", resultado);
    }

}