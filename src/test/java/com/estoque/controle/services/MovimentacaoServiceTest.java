package com.estoque.controle.services;

import com.estoque.controle.exceptions.EstoqueInsuficienteException;
import com.estoque.controle.exceptions.ProdutoNaoEncontradoException;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.fornecedor.TipoFornecedor;
import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.usuario.NivelDeUsuario;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.repository.MovimentacaoRepository;
import com.estoque.controle.repository.ProdutoRepository;
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
class MovimentacaoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private ConfigEstoqueService configEstoqueService;

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Test
    @DisplayName("Lançar uma movimentação de entrada")
    void deveRealizarUmaMovimentacaoDeEntrada() {
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

        configEstoqueService.atualizaStatus(1L);

        Movimentacao novaMovimentacao = new Movimentacao();
        novaMovimentacao.setId(1L);
        novaMovimentacao.setDataHora(LocalDateTime.now());
        novaMovimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        novaMovimentacao.setProduto(produto);
        novaMovimentacao.setUsuario(usuario);
        novaMovimentacao.setQuantidade(5);
        novaMovimentacao.setDescricao("123");

        Mockito.when(movimentacaoRepository.save(Mockito.any(Movimentacao.class))).thenReturn(novaMovimentacao);

        Movimentacao resultado = movimentacaoService.registrarMovimentacao(1L, 5, TipoMovimentacao.ENTRADA, "123", usuario);

        assertNotNull(resultado.getId());
        assertEquals(15, produto.getQuantidade());


        Mockito.verify(movimentacaoRepository, Mockito.times(1)).save(Mockito.any(Movimentacao.class));
    }

    @Test
    @DisplayName("Lançar uma movimentação de saida")
    void deveRealizarUmaMovimentacaoDeSaida() {
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

        configEstoqueService.atualizaStatus(1L);

        Movimentacao novaMovimentacao = new Movimentacao();
        novaMovimentacao.setId(1L);
        novaMovimentacao.setDataHora(LocalDateTime.now());
        novaMovimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        novaMovimentacao.setProduto(produto);
        novaMovimentacao.setUsuario(usuario);
        novaMovimentacao.setQuantidade(5);
        novaMovimentacao.setDescricao("123");

        Mockito.when(movimentacaoRepository.save(Mockito.any(Movimentacao.class))).thenReturn(novaMovimentacao);

        Movimentacao resultado = movimentacaoService.registrarMovimentacao(1L, 5, TipoMovimentacao.SAIDA, "123", usuario);

        assertNotNull(resultado.getId());
        assertEquals(5, produto.getQuantidade());


        Mockito.verify(movimentacaoRepository, Mockito.times(1)).save(Mockito.any(Movimentacao.class));
    }

    @Test
    @DisplayName("Lançar uma exceção de produto não encontrado")
    void deveLancarUmaExcecaoProdutoNaoEncontrado() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        configEstoqueService.atualizaStatus(1L);

        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            movimentacaoService.registrarMovimentacao(1000L, 5, TipoMovimentacao.SAIDA, "123", usuario);
        });
    }

    @Test
    @DisplayName("Lançar uma exceção de estoque insuficiente")
    void deveLancarUmaExcecaoEstoqueInsuficiente() {
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

        configEstoqueService.atualizaStatus(1L);

        assertThrows(EstoqueInsuficienteException.class, () -> {
            movimentacaoService.registrarMovimentacao(1L, 100, TipoMovimentacao.SAIDA, "123", usuario);
        });
    }
}