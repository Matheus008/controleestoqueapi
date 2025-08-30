package com.estoque.controle.controller;

import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.relatorios.FiltroRelatorioDTO;
import com.estoque.controle.model.relatorios.RelatorioMovimentacaoDTO;
import com.estoque.controle.model.relatorios.RelatorioVendaDTO;
import com.estoque.controle.services.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/relatorio")
@Tag(name = "Relatorios", description = "Criar relatorios personalizados")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(summary = "Relatorios de movimentação", description = "Poderá criar relatórios personalizados de movimentação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("/movimentacoes")
    public List<RelatorioMovimentacaoDTO> relatorioMovimentacoes(
            @RequestParam(required = false) LocalDateTime inicio,
            @RequestParam(required = false) LocalDateTime fim,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idProduto,
            @RequestParam(required = false) TipoMovimentacao tipoMovimentacao
    ) {
        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, fim, idUsuario, idProduto, null, tipoMovimentacao);
        return relatorioService.relatioriDeMovimentacao(filtro);
    }

    @Operation(summary = "Relatorios de vendas", description = "Poderá criar relatórios personalizados de vendas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("/vendas")
    public List<RelatorioVendaDTO> relatorioVendas(
            @RequestParam(required = false) LocalDateTime inicio,
            @RequestParam(required = false) LocalDateTime fim,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idproduto,
            @RequestParam(required = false) Long idCliente
    ) {
        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, fim, idUsuario, idproduto, idCliente, null);
        return relatorioService.relatorioDeVenda(filtro);
    }

}
