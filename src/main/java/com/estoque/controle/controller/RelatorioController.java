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
import jakarta.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicioDateTime, fimDateTime, idUsuario, idproduto, idCliente, null);
        return relatorioService.relatorioDeVenda(filtro);
    }

    @GetMapping("/vendas/indicadores")
    public Map<String, Object> indicadores(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim
    ) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

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
    ) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        VendaIndicadoresDTO indicadoresDTO = vendaRepository.calcularIndicadores(inicioDateTime, fimDateTime);
        List<VendedorRankingDTO> ranking = usuarioRepository.quemVendeuMais(inicioDateTime, fimDateTime);

        return Map.of(
                "Quantidade Total Vendido:", indicadoresDTO.getQuantidadeTotalVendida(),
                "Faturamento Total:", indicadoresDTO.getFaturamentoTotal(),
                "Ranking dos Vendedores:", ranking
        );
    }

    @GetMapping("/export/pdf")
    public void exportarEmPdf(HttpServletResponse response,
                              @RequestParam LocalDate inicio,
                              @RequestParam LocalDate fim,
                              @RequestParam(required = false) boolean rankingProduto,
                              @RequestParam(required = false) boolean rankingVendedor,
                              @RequestParam(required = false) boolean calcularIndicadores) throws IOException {

        if (!rankingProduto && !rankingVendedor && !calcularIndicadores) {
            throw new IOException("Deverá ter pelo menos um parâmetro True");
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_e_ranking.pdf");

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        VendaIndicadoresDTO indicadoresDTO = vendaRepository.calcularIndicadores(inicioDateTime, fimDateTime);
        List<ProdutoRankingDTO> produtoRankingDTOS = produtoRepository.rankingProdutos(inicioDateTime, fimDateTime);
        List<VendedorRankingDTO> vendedorRankingDTOS = usuarioRepository.quemVendeuMais(inicioDateTime, fimDateTime);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Relatorio General");
            contentStream.endText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

            int yPosicao = 720;

            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosicao);
            contentStream.showText("Periodo: " + inicio + " até " + fim);
            contentStream.endText();

            yPosicao -= 20;

            if (calcularIndicadores) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosicao);
                contentStream.showText("Quantidade total vendido: " + indicadoresDTO.getQuantidadeTotalVendida());
                contentStream.endText();

                yPosicao -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosicao);
                contentStream.showText("Faturamento total: R$" + indicadoresDTO.getFaturamentoTotal());
                contentStream.endText();

                yPosicao -= 40;
            }

            if (rankingProduto) {
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosicao);
                contentStream.showText("Ranking de Produtos");
                contentStream.endText();

                yPosicao -= 20;

                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                for (ProdutoRankingDTO p : produtoRankingDTOS) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosicao);
                    contentStream.showText(p.getProduto() + " - Quantidade: " + p.getQuantidadeVendida());
                    contentStream.endText();
                    yPosicao -= 20;

                    if (yPosicao < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    }
                }

            }

            if (rankingVendedor) {
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosicao);
                contentStream.showText("Ranking dos Vendedores");
                contentStream.endText();

                yPosicao -= 20;

                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                for (VendedorRankingDTO v : vendedorRankingDTOS) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosicao);
                    contentStream.showText(v.getNomeUsuario() + " - Quantidade: " + v.getQuantidadeVendida());
                    contentStream.endText();
                    yPosicao -= 20;

                    if (yPosicao < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    }
                }
            }

            contentStream.close();

            document.save(response.getOutputStream());
        }

    }

    @GetMapping("/export/excel")
    public void exportarEmExcel(HttpServletResponse response,
                                @RequestParam LocalDate inicio,
                                @RequestParam LocalDate fim,
                                @RequestParam(required = false) boolean rankingProduto,
                                @RequestParam(required = false) boolean rankingVendedor,
                                @RequestParam(required = false) boolean calcularIndicadores) throws Exception {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_e_ranking.xlsx");

        if(!rankingProduto && !rankingVendedor && !calcularIndicadores) {
            throw new Exception("Selecione pelo menos um valor");
        }

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        VendaIndicadoresDTO indicadoresDTO = vendaRepository.calcularIndicadores(inicioDateTime, fimDateTime);
        List<ProdutoRankingDTO> produtoRankingDTOS = produtoRepository.rankingProdutos(inicioDateTime, fimDateTime);
        List<VendedorRankingDTO> vendedorRankingDTOS = usuarioRepository.quemVendeuMais(inicioDateTime, fimDateTime);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Relatório");

        int quantidadeDeLinhas = 0;

        Row row1 = sheet.createRow(quantidadeDeLinhas);
        row1.createCell(0).setCellValue("inicio: "+ inicio);
        row1.createCell(1).setCellValue("fim: "+ fim);

        if(calcularIndicadores) {

            Row totalVendidoRow = sheet.createRow(++quantidadeDeLinhas);
            totalVendidoRow.createCell(0).setCellValue("Quantidade Total Vendida");
            totalVendidoRow.createCell(1).setCellValue(indicadoresDTO.getQuantidadeTotalVendida());

            Row totalFaturadoRow = sheet.createRow(++quantidadeDeLinhas);
            totalFaturadoRow.createCell(0).setCellValue("Quantidade Total Faturado");
            totalFaturadoRow.createCell(0).setCellValue(indicadoresDTO.getFaturamentoTotal());
        }

        if(rankingProduto) {

            Row headerProduto = sheet.createRow(++quantidadeDeLinhas);
            headerProduto.createCell(0).setCellValue("Produto");
            headerProduto.createCell(1).setCellValue("Quantidade Vendida");

            quantidadeDeLinhas++;
            for(ProdutoRankingDTO pDTO : produtoRankingDTOS) {
                Row row = sheet.createRow(quantidadeDeLinhas++);
                row.createCell(0).setCellValue(pDTO.getProduto());
                row.createCell(1).setCellValue(pDTO.getQuantidadeVendida());
            }
        }

        if(rankingVendedor) {
            Row headerVendedor = sheet.createRow(++quantidadeDeLinhas);
            headerVendedor.createCell(0).setCellValue("Vendedor");
            headerVendedor.createCell(1).setCellValue("Quantidade Vendida");

            quantidadeDeLinhas++;
            for(VendedorRankingDTO vDTO : vendedorRankingDTOS) {
                Row row = sheet.createRow(quantidadeDeLinhas++);
                row.createCell(0).setCellValue(vDTO.getNomeUsuario());
                row.createCell(1).setCellValue(vDTO.getQuantidadeVendida());
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
