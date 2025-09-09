package com.estoque.controle.controller;

import com.estoque.controle.exceptions.FornecedorNaoEncontradoException;
import com.estoque.controle.exceptions.ProdutoNaoEncontradoException;
import com.estoque.controle.exceptions.ProdutoNaoEstaZeradoException;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.dto.ProdutoDTO;
import com.estoque.controle.repository.FornecedorRepository;
import com.estoque.controle.repository.ProdutoRepository;
import com.estoque.controle.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("produtos")
@Tag(name = "Produto", description = "Gerenciamento dos produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoController(ProdutoRepository produtoRepository, ProdutoService produtoService, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Operation(summary = "Salvar produto", description = "Cadastrar produto no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PostMapping
    public Produto salvar(@RequestBody ProdutoDTO produtoDTO) {
        return produtoService.registrarProduto(produtoDTO.nome(), produtoDTO.descricao(), produtoDTO.preco(), produtoDTO.idFornecedor());
    }

    @Operation(summary = "Deletar produto", description = "Deletar produto do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @DeleteMapping("{id}")
    public void deletar(@PathVariable("id") Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(ProdutoNaoEncontradoException::new);

        if (produto.getQuantidade() != 0) {
            throw new ProdutoNaoEstaZeradoException();
        } else {
            produtoRepository.deleteById(id);
        }
    }

    @Operation(summary = "Atualizar produto", description = "Atualizar o produto no bando de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto alterado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @PutMapping("{id}")
    public void atualizar(@PathVariable("id") Long id, @RequestBody ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id).orElseThrow(ProdutoNaoEncontradoException::new);

        produto.setId(id);
        produto.setNome(produtoDTO.nome());
        produto.setPreco(produtoDTO.preco());
        produto.setDescricao(produtoDTO.descricao());
        Fornecedor fornecedor = fornecedorRepository.findById(produtoDTO.idFornecedor()).orElseThrow(FornecedorNaoEncontradoException::new);
        produto.setFornecedor(fornecedor);

        produtoRepository.save(produto);
    }

    @Operation(summary = "Buscar produtos", description = "Buscar todos os produtos do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca de produto realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping
    public List<Produto> buscar(@RequestParam(value = "nome", required = false) String nome) {
        if (nome != null) {
            return produtoRepository.findByNome(nome);
        }
        return produtoRepository.findAll();
    }

    @Operation(summary = "Buscar produto por Id", description = "Buscar produto por Id no banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca de produto por Id realizada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para executar essa ação")
    })
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable("id") Long id) {
        return produtoRepository.findById(id).orElse(null);
    }
}
