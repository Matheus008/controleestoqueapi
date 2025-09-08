package com.estoque.controle.controller;

import com.estoque.controle.dto.ConfigEstoqueDTO;
import com.estoque.controle.dto.InventarioDTO;
import com.estoque.controle.model.produto.ConfigEstoque;
import com.estoque.controle.services.ConfigEstoqueService;
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

    @PostMapping("criar")
    public ConfigEstoque criarConfigEstoque(@RequestBody @Valid ConfigEstoqueDTO configEstoqueDTO) {
        return configEstoqueService.configurarEstoque(configEstoqueDTO.estoqueMinimo(), configEstoqueDTO.estoqueMaximo(), configEstoqueDTO.produto_id());
    }

    @GetMapping("inventario")
    public List<InventarioDTO> inventario() {
        return configEstoqueService.inventario();
    }

    @GetMapping("estoque-minimo")
    public List<String> estoqueMinimo() {
        return configEstoqueService.produtosAbaixoDoMinimo();
    }

    @GetMapping("alertas")
    public List<String> alertas() {
        return configEstoqueService.alertaEstoque();
    }
}
