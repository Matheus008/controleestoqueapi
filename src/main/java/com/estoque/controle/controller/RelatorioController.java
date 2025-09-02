package com.estoque.controle.controller;

import com.estoque.controle.dto.ProdutoRankingDTO;
import com.estoque.controle.dto.VendaIndicadoresDTO;
import com.estoque.controle.dto.VendedorRankingDTO;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.dto.relatorios.FiltroRelatorioDTO;
import com.estoque.controle.dto.relatorios.RelatorioMovimentacaoDTO;
import com.estoque.controle.dto.relatorios.RelatorioVendaDTO;
import com.estoque.controle.repository.ProdutoRepository;
import com.estoque.controle.repository.UsuarioRepository;
import com.estoque.controle.repository.VendaRepository;
import com.estoque.controle.services.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorio")
@Tag(name = "Relatorios", description = "Criar relatorios personalizados")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatorioController(RelatorioService relatorioService, VendaRepository vendaRepository, ProdutoRepository produtoRepository, UsuarioRepository usuarioRepository) {
        this.relatorioService = relatorioService;
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
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
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idproduto,
            @RequestParam(required = false) Long idCliente
    ) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23,59,59);

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicioDateTime, fimDateTime, idUsuario, idproduto, idCliente, null);
        return relatorioService.relatorioDeVenda(filtro);
    }

    @GetMapping("/vendas/indicadores")
    public Map<String, Object> indicadores(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
            ){

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23,59,59);

        VendaIndicadoresDTO indicadoresDTO = vendaRepository.calcularIndicadores(inicioDateTime, fimDateTime);
        List<ProdutoRankingDTO> ranking = produtoRepository.rankingProdutos(inicioDateTime, fimDateTime);

        return Map.of(
                "Quantidade Total Vendido:", indicadoresDTO.getQuantidadeTotalVendida(),
                "Faturamento Total:", indicadoresDTO.getFaturamentoTotal(),
                "Ranking dos Produtos", ranking
        );
    }

    @GetMapping("/vendas/ranking")
    public Map<String, Object> rankingVendedor(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
            ){

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23,59,59);

        VendaIndicadoresDTO indicadoresDTO = vendaRepository.calcularIndicadores(inicioDateTime, fimDateTime);
        List<VendedorRankingDTO> ranking = usuarioRepository.quemVendeuMais(inicioDateTime, fimDateTime);

        return Map.of(
                "Quantidade Total Vendido:", indicadoresDTO.getQuantidadeTotalVendida(),
                "Faturamento Total:", indicadoresDTO.getFaturamentoTotal(),
                "Ranking dos Vendedores:", ranking
        );
    }

}
