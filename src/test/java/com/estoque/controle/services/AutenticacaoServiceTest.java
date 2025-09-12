package com.estoque.controle.services;


import com.estoque.controle.repository.UsuarioRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve autenticar o usuário")
    void deveAutenticarOUsuario() {

        String email = "teste@email.com";

        UserDetails usuario = User.withUsername(email)
                .password("123456")
                .roles("USER")
                .build();

        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(usuario);

        UserDetails resultado = usuarioRepository.findByEmail(email);

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getUsername());

        Mockito.verify(usuarioRepository, Mockito.times(1)).findByEmail(email);
    }
}