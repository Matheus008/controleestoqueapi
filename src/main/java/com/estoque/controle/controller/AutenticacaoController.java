package com.estoque.controle.controller;

import com.estoque.controle.model.usuario.AutenticacaoDTO;
import com.estoque.controle.model.usuario.LoginResponseDTO;
import com.estoque.controle.model.usuario.RegisterDTO;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.repository.UsuarioRepository;
import com.estoque.controle.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Autenticar o usuário atrávez de token.")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Operation(summary = "Login", description = "Validar o email e a senha do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email e senha validado!"),
            @ApiResponse(responseCode = "403", description = "Email ou senha inválido!")
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AutenticacaoDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Registrar", description = "Registrar o usuário.")
    @ApiResponse(responseCode = "200", description = "Cadastrado com sucesso.")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedSenha = new BCryptPasswordEncoder().encode(data.senha());

        Usuario novoUsuario = new Usuario(data.email(), encryptedSenha, data.nivelDeUsuario(), data.nomeUsuario());

        this.repository.save(novoUsuario);

        return ResponseEntity.ok().build();
    }
}
