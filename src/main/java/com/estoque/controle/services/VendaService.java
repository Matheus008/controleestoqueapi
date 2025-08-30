package com.estoque.controle.services;

import com.estoque.controle.model.cliente.Cliente;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.model.vendas.Venda;
import com.estoque.controle.repository.ClienteRepository;
import com.estoque.controle.repository.ProdutoRepository;
import com.estoque.controle.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendaService {

    private ProdutoRepository produtoRepository;
    private VendaRepository vendaRepository;
    private MovimentacaoService movimentacaoService;
    private ClienteRepository clienteRepository;

    public VendaService(ProdutoRepository produtoRepository,
                        VendaRepository vendaRepository,
                        MovimentacaoService movimentacaoService,
                        ClienteRepository clienteRepository) {
        this.produtoRepository = produtoRepository;
        this.vendaRepository = vendaRepository;
        this.movimentacaoService = movimentacaoService;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Venda registrarVenda(Long produtoId, int quantidade, Long clienteId, Usuario usuario) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Cliente cliente =  clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if(produto.getQuantidade() < quantidade) {
            throw new RuntimeException("Quantidade inválida");
        }


        Venda venda = new Venda();
        venda.setUsuario(usuario);
        venda.setQuantidade(quantidade);
        venda.setValorTotalVendido((produto.getPreco() * quantidade) * 1.05); // provisório
        venda.setProduto(produto);
        String descricao = "Venda realizada para o cliente: " + cliente.getNomeCliente();
        venda.setMovimentacao(movimentacaoService.registrarMovimentacao(produtoId, quantidade, TipoMovimentacao.SAIDA, descricao, usuario));
        venda.setCliente(cliente);

        return  vendaRepository.save(venda);

    }
}
