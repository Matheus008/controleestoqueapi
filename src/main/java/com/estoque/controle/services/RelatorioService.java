package com.estoque.controle.services;

import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.dto.relatorios.FiltroRelatorioDTO;
import com.estoque.controle.dto.relatorios.RelatorioMovimentacaoDTO;
import com.estoque.controle.dto.relatorios.RelatorioVendaDTO;
import com.estoque.controle.model.vendas.Venda;
import com.estoque.controle.repository.MovimentacaoRepository;
import com.estoque.controle.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RelatorioService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final VendaRepository vendaRepository;

    public RelatorioService(MovimentacaoRepository movimentacaoRepository, VendaRepository vendaRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.vendaRepository = vendaRepository;
    }

    public List<RelatorioMovimentacaoDTO> relatorioDeMovimentacao(FiltroRelatorioDTO filtro) {
        List<Movimentacao> movimentacoes;

        if(filtro.inicio() != null && filtro.fim() != null) {
            movimentacoes = movimentacaoRepository.findByDataHoraBetween(filtro.inicio(), filtro.fim());
        } else if(filtro.inicio() != null){
            movimentacoes = movimentacaoRepository.findByDataHoraAfter(filtro.inicio());
        } else if (filtro.fim() != null) {
            movimentacoes = movimentacaoRepository.findByDataHoraBefore(filtro.fim());
        } else {
            movimentacoes = movimentacaoRepository.findAll();
        }

        return movimentacoes.stream()
                .filter(movimentacao -> filtro.usuarioId() == null || Objects.equals(movimentacao.getUsuario().getId(), filtro.usuarioId()))
                .filter(movimentacao -> filtro.produtoId() == null || Objects.equals(movimentacao.getProduto().getId(), filtro.produtoId()))
                .filter(movimentacao -> filtro.tipoMovimentacao() == null || Objects.equals(movimentacao.getTipoMovimentacao(), filtro.tipoMovimentacao()))
                .map(movimentacao ->new RelatorioMovimentacaoDTO(
                        movimentacao.getId(),
                        movimentacao.getProduto().getNome(),
                        movimentacao.getQuantidade(),
                        movimentacao.getTipoMovimentacao(),
                        movimentacao.getDescricao(),
                        movimentacao.getUsuario().getNomeUsuario(),
                        movimentacao.getDataHora()
                ))
                .toList();
    }

    public List<RelatorioVendaDTO> relatorioDeVenda(FiltroRelatorioDTO filtro) {
        List<Venda> vendas;

        if(filtro.inicio() != null && filtro.fim() != null) {
            vendas = vendaRepository.findByDataVendaBetween(filtro.inicio(), filtro.fim());
        } else if(filtro.inicio() != null) {
            vendas = vendaRepository.findByDataVendaAfter(filtro.inicio());
        } else if (filtro.fim() != null) {
            vendas = vendaRepository.findByDataVendaBefore(filtro.fim());
        } else {
            vendas = vendaRepository.findAll();
        }

        return vendas.stream()
                .filter(venda -> filtro.produtoId() == null || Objects.equals(venda.getProduto().getId(), filtro.produtoId()))
                .filter(venda -> filtro.clienteId() == null || Objects.equals(venda.getCliente().getId(), filtro.clienteId()))
                .filter(venda -> filtro.usuarioId() == null || Objects.equals(venda.getUsuario().getId(), filtro.usuarioId()))
                .map(venda -> new RelatorioVendaDTO(
                        venda.getId(),
                        venda.getProduto().getNome(),
                        venda.getQuantidade(),
                        venda.getValorTotalVendido(),
                        venda.getProduto().getPreco(),
                        venda.getCliente().getNomeCliente(),
                        venda.getUsuario().getNomeUsuario(),
                        venda.getDataVenda()
                ))
                .toList();
    }
}
