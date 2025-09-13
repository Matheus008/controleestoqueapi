package com.estoque.controle.controller;

import com.estoque.controle.dto.ConfigEstoqueDTO;
import com.estoque.controle.dto.InventarioDTO;
import com.estoque.controle.model.produto.ConfigEstoque;
import com.estoque.controle.services.ConfigEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/config-estoque")
public class ConfigEstoqueController {

    private final ConfigEstoqueService configEstoqueService;

    public ConfigEstoqueController(ConfigEstoqueService configEstoqueService) {
        this.configEstoqueService = configEstoqueService;
    }

    @Operation(summary = "Criar uma configuração para o estoque", description = "poderá criar configurações personalizadas para o estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Configuração criada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PostMapping("criar")
    public ConfigEstoque criarConfigEstoque(@RequestBody @Valid ConfigEstoqueDTO configEstoqueDTO) {
        return configEstoqueService.configurarEstoque(configEstoqueDTO.estoqueMinimo(), configEstoqueDTO.estoqueMaximo(), configEstoqueDTO.produto_id());
    }

    @Operation(summary = "Retorna as informações sobre o estoque", description = "irá retornar o nome do produto, quantidade atual, estoque minimo e maximo e o status atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso!"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("inventario")
    public List<InventarioDTO> inventario() {
        return configEstoqueService.inventario();
    }

    @Operation(summary = "Retorna os produtos com o estoque abaixo", description = "irá retornar o nome do produto que está com o estoque abaixo do minimo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("estoque-minimo")
    public List<String> estoqueMinimo() {
        return configEstoqueService.produtosAbaixoDoMinimo();
    }

    @Operation(summary = "Retorna um alerta", description = "irá retornar um alerta do produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("alertas")
    public List<String> alertas() {
        return configEstoqueService.alertaEstoque();
    }
}
