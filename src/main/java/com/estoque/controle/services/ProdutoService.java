package com.estoque.controle.services;

import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.repository.FornecedorRepository;
import com.estoque.controle.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional
    public Produto registrarProduto(String nome, String descricao, Double preco, Long idFornecedor) {
        Fornecedor fornecedor = fornecedorRepository.findById(idFornecedor).orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setQuantidade(0);
        produto.setValorTotal(0.0);
        produto.setFornecedor(fornecedor);

        return produtoRepository.save(produto);
    }

    public Double calcularValorTotal(int quantidade, double preco) {
        return quantidade * preco;
    }
}
