package com.estoque.controle.services;

import com.estoque.controle.dto.InventarioDTO;
import com.estoque.controle.model.produto.ConfigEstoque;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.StatusEstoque;
import com.estoque.controle.repository.ConfigEstoqueRepository;
import com.estoque.controle.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigEstoqueService {

    private final ConfigEstoqueRepository configEstoqueRepository;
    private final ProdutoRepository produtoRepository;

    public ConfigEstoqueService(ConfigEstoqueRepository configEstoqueRepository, ProdutoRepository produtoRepository) {
        this.configEstoqueRepository = configEstoqueRepository;
        this.produtoRepository = produtoRepository;
    }

    public ConfigEstoque configurarEstoque(int estoqueMinimo, int estoqueMaximo, Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ConfigEstoque configEstoque = configEstoqueRepository.findByProdutoId(idProduto).orElse(new ConfigEstoque());

        configEstoque.setEstoqueMinimo(estoqueMinimo);
        configEstoque.setEstoqueMaximo(estoqueMaximo);
        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(verificaStatus(estoqueMinimo, estoqueMaximo, produto.getQuantidade()));

        return configEstoqueRepository.save(configEstoque);
    }

    public void atualizaStatus(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ConfigEstoque configEstoque = configEstoqueRepository.findByProdutoId(idProduto).orElseThrow();

        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(verificaStatus(configEstoque.getEstoqueMinimo(), configEstoque.getEstoqueMaximo(), produto.getQuantidade()));

        configEstoqueRepository.save(configEstoque);
    }

    public StatusEstoque verificaStatus(int estoqueMinimo, int estoqueMaximo, int quantidadeEstoque) {
        if (quantidadeEstoque <= estoqueMinimo) {
            return StatusEstoque.ABAIXO;
        } else if (quantidadeEstoque >= estoqueMaximo) {
            return StatusEstoque.MAXIMO;
        } else {
            return StatusEstoque.NORMAL;
        }
    }

    public List<InventarioDTO> inventario() {
        List<ConfigEstoque> configEstoque = configEstoqueRepository.findAll();

        return configEstoque.stream()
                .map(inventario -> new InventarioDTO(
                        inventario.getProduto().getNome(),
                        inventario.getProduto().getQuantidade(),
                        inventario.getEstoqueMinimo(),
                        inventario.getEstoqueMaximo(),
                        inventario.getStatusEstoque()
                ))
                .toList();
    }

    public List<String> produtosAbaixoDoMinimo() {
        List<ConfigEstoque> configEstoque = configEstoqueRepository.findAll();

        return configEstoque.stream()
                .filter(abaixo -> abaixo.getStatusEstoque() == StatusEstoque.ABAIXO)
                .map(produto -> produto.getProduto().getNome())
                .toList();
    }

    public List<String> alertaEstoque() {
        List<ConfigEstoque> configEstoque = configEstoqueRepository.findAll();
        List<String> alertas = new ArrayList<>();

        for(ConfigEstoque config : configEstoque) {
            if(config.getProduto().getQuantidade() <= config.getEstoqueMinimo()) {
                alertas.add("⚠ Produto "+ config.getProduto().getNome() +" a baixo do estoque minimo ("+ config.getProduto().getQuantidade() +"/"+ config.getEstoqueMinimo() +")");
            }else if(config.getProduto().getQuantidade() >= config.getEstoqueMaximo()) {
                alertas.add("⚠ Produto "+ config.getProduto().getNome() +" a cima do estoque maximo ("+ config.getProduto().getQuantidade() +"/"+ config.getEstoqueMaximo() +")");
            }
        }

        return alertas;
    }
}
