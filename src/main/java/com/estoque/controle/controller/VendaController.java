package com.estoque.controle.controller;

import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.model.vendas.Venda;
import com.estoque.controle.dto.VendasDTO;
import com.estoque.controle.repository.UsuarioRepository;
import com.estoque.controle.services.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/vendas")
@Tag(name = "Vendas", description = "Registrar venda dos produtos")
public class VendaController {

    private final VendaService vendaService;
    private final UsuarioRepository usuarioRepository;

    public VendaController(VendaService vendaService, UsuarioRepository usuarioRepository) {
        this.vendaService = vendaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Registrar venda de um produto", description = "Registrar venda de um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda registrada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PostMapping
    public Venda registrar(@RequestBody VendasDTO vendasDTO, Principal principal) {
        Usuario usuario = (Usuario) usuarioRepository.findByEmail(principal.getName());

        return vendaService.registrarVenda(vendasDTO.produtoId(),
                vendasDTO.quantidade(), vendasDTO.clienteId(), usuario);
    }
}
