package com.estoque.controle.controller;

import com.estoque.controle.exceptions.CampoVazioException;
import com.estoque.controle.exceptions.UsuarioNaoEncontradoException;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
@Tag(name = "Usuario", description = "Gerenciamento de usuários")
public class UsuarioController {

    UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Deleta usuários", description = "Deletar usuário dependendo do nível de permoissão.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @DeleteMapping("{id}")
    public void deletarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(UsuarioNaoEncontradoException::new);

        usuarioRepository.deleteById(id);
    }

    @Operation(summary = "Atualizar usuários", description = "Alterar/Atualizar os dados, nível de permissão ou senha do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PutMapping("{id}")
    public void atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        usuarioRepository.save(usuario);
    }

    @Operation(summary = "Lista usuários cadastrados", description = "Lista todos os usuários cadastrados dentro do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca de usuarios realida com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping
    public List<Usuario> buscarTodoOuPorNome(@PathVariable(value = "nome", required = false) String nome) {
        if(nome != null) {
            return  usuarioRepository.findByNomeUsuario(nome);
        }
        return usuarioRepository.findAll();
    }

    @Operation(summary = "Buscar usuario por email", description = "Fazer uma busca no banco de dados para procurar o email do usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca por email realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("{email}")
    public UserDetails buscarPorEmail(@PathVariable String email) {
        if(email == null) {
            throw new CampoVazioException();
        }
        return usuarioRepository.findByEmail(email);
    }
}
