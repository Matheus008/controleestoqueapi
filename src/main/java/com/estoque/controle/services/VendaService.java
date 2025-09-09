package com.estoque.controle.services;

import com.estoque.controle.exceptions.ClienteNaoEncontradoException;
import com.estoque.controle.exceptions.EstoqueInsuficienteException;
import com.estoque.controle.exceptions.ProdutoNaoEncontradoException;
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

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;
    private final MovimentacaoService movimentacaoService;
    private final ClienteRepository clienteRepository;

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
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(ProdutoNaoEncontradoException::new);
        Cliente cliente =  clienteRepository.findById(clienteId).orElseThrow(ClienteNaoEncontradoException::new);

        if(produto.getQuantidade() < quantidade) {
            throw new EstoqueInsuficienteException(produto.getNome(), quantidade, produto.getQuantidade());
        }


        Venda venda = new Venda();
        venda.setUsuario(usuario);
        venda.setQuantidade(quantidade);
        venda.setValorTotalVendido((produto.getPreco() * quantidade) * 1.05);
        venda.setProduto(produto);
        String descricao = "Venda realizada para o cliente: " + cliente.getNomeCliente();
        venda.setMovimentacao(movimentacaoService.registrarMovimentacao(produtoId, quantidade, TipoMovimentacao.SAIDA, descricao, usuario));
        venda.setCliente(cliente);

        return  vendaRepository.save(venda);

    }
}
