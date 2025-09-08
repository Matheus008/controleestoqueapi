package com.estoque.controle.services;

import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.repository.MovimentacaoRepository;
import com.estoque.controle.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovimentacaoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoService produtoService;
    private final ConfigEstoqueService configEstoqueService;


    public MovimentacaoService(ProdutoRepository produtoRepository, MovimentacaoRepository movimentacaoRepository, ProdutoService produtoService, ConfigEstoqueService configEstoqueService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
        this.configEstoqueService = configEstoqueService;
    }

    @Transactional
    public Movimentacao registrarMovimentacao(Long produtoId, int quantidade, TipoMovimentacao tipoMovimentacao, String descricao, Usuario usuario) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (tipoMovimentacao == TipoMovimentacao.SAIDA) {
            if (produto.getQuantidade() < quantidade) {
                throw new RuntimeException("Quantidade de saida maior que o estoque!");
            }
            produto.setQuantidade(produto.getQuantidade() - quantidade);
        } else {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
        }
        configEstoqueService.atualizaStatus(produtoId);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipoMovimentacao(tipoMovimentacao);
        movimentacao.setDescricao(descricao);
        movimentacao.setUsuario(usuario);


        produto.setValorTotal(produtoService.calcularValorTotal(produto.getQuantidade(), produto.getPreco()));

        produtoRepository.save(produto);
        return movimentacaoRepository.save(movimentacao);

    }

}
