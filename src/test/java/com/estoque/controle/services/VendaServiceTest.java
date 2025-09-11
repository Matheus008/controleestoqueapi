package com.estoque.controle.services;

import com.estoque.controle.exceptions.ClienteNaoEncontradoException;
import com.estoque.controle.exceptions.EstoqueInsuficienteException;
import com.estoque.controle.exceptions.ProdutoNaoEncontradoException;
import com.estoque.controle.model.cliente.Cliente;
import com.estoque.controle.model.cliente.TipoCliente;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.fornecedor.TipoFornecedor;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.usuario.NivelDeUsuario;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.model.vendas.Venda;
import com.estoque.controle.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private MovimentacaoService movimentacaoService;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VendaService vendaService;

    @Test
    @DisplayName("Realizar uma venda de um produto")
    void deveRealizarUmaVenda() {

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Tv Samsung");
        produto.setDescricao("Tv pirata");
        produto.setPreco(1000.00);
        produto.setQuantidade(10);
        produto.setValorTotal(produto.getPreco() * produto.getQuantidade());
        produto.setFornecedor(fornecedor);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Venda novaVenda = new Venda();
        novaVenda.setId(1L);
        novaVenda.setQuantidade(5);
        novaVenda.setCliente(cliente);
        novaVenda.setUsuario(usuario);
        novaVenda.setDataVenda(LocalDateTime.now());
        novaVenda.setProduto(produto);
        novaVenda.setValorTotalVendido((produto.getPreco() * novaVenda.getQuantidade()) * 1.05);
        novaVenda.setMovimentacao(movimentacaoService.registrarMovimentacao(1L, 5, TipoMovimentacao.SAIDA,"123", usuario));

        Mockito.when(vendaRepository.save(Mockito.any(Venda.class))).thenReturn(novaVenda);

        Venda resultado = vendaService.registrarVenda(1L, 5, 1L, usuario);

        assertNotNull(resultado.getId());
        assertEquals(5, resultado.getQuantidade());
        assertEquals(cliente.getId(), resultado.getCliente().getId());

        Mockito.verify(clienteRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(vendaRepository, Mockito.times(1)).save(Mockito.any(Venda.class));
    }

    @Test
    @DisplayName("Lançar uma exceção para produto não encontrado")
    void deveLancarUmaExcecaoQuandoOProdutoNaoExiste() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            vendaService.registrarVenda(99L, 5, 1L, usuario);
        });
    }

    @Test
    @DisplayName("Lançar uma exceção para cliente não encontrado")
    void deveLancarUmaExcecaoQuandoOClienteNaoExiste() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Tv Samsung");
        produto.setDescricao("Tv pirata");
        produto.setPreco(1000.00);
        produto.setQuantidade(10);
        produto.setValorTotal(produto.getPreco() * produto.getQuantidade());
        produto.setFornecedor(fornecedor);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);
        assertThrows(ClienteNaoEncontradoException.class, () -> {
            vendaService.registrarVenda(1L, 5, 99L, usuario);
        });
    }

    @Test
    @DisplayName("Lançar uma exceção para estoque insuficiente")
    void deveLancarUmaExcecaoQuandoEstoqueInsuficiente() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Tv Samsung");
        produto.setDescricao("Tv pirata");
        produto.setPreco(1000.00);
        produto.setQuantidade(10);
        produto.setValorTotal(produto.getPreco() * produto.getQuantidade());
        produto.setFornecedor(fornecedor);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        assertThrows(EstoqueInsuficienteException.class, () -> {
            vendaService.registrarVenda(1L, 500, 1L, usuario);
        });
    }
}