package com.estoque.controle.services;

import com.estoque.controle.exceptions.FornecedorNaoEncontradoException;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.fornecedor.TipoFornecedor;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.repository.FornecedorRepository;
import com.estoque.controle.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Teste para cadastrar produtos com fornecedor")
    void deveRetornarUmProduto() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Mockito.when(fornecedorRepository.findById(1L)).thenReturn(Optional.of(fornecedor));

        Produto novoProduto = new Produto();
        novoProduto.setId(1L);
        novoProduto.setNome("Tv Samsung");
        novoProduto.setDescricao("Tv pirata");
        novoProduto.setPreco(1000.00);
        novoProduto.setQuantidade(0);
        novoProduto.setValorTotal(0.0);
        novoProduto.setFornecedor(fornecedor);

        Mockito.when(produtoRepository.save(Mockito.any(Produto.class))).thenReturn(novoProduto);

        Produto resultado = produtoService.registrarProduto("Tv Samsung", "Tv pirata", 1000.00, 1L);

        assertNotNull(resultado.getId());
        assertEquals("Tv Samsung", resultado.getNome());
        assertEquals(0, resultado.getQuantidade());
        assertEquals(0.0, resultado.getValorTotal());
        assertEquals(fornecedor.getId(), resultado.getFornecedor().getId());

        Mockito.verify(fornecedorRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(produtoRepository, Mockito.times(1)).save(Mockito.any(Produto.class));

    }

    @Test
    @DisplayName("Teste para lançar uma exceção de fornecedor não encontrado")
    void deveLancarUmaExcecaoQuandoFornecedorNaoExiste() {
        assertThrows(FornecedorNaoEncontradoException.class, () -> {
            produtoService.registrarProduto("Teste", "teste do teste", 1000.0, 123L);
        });
    }

    @Test
    @DisplayName("Teste para calcular o valor total dos produtos")
    void deveCalcularValorCorreto() {
        double valor = produtoService.calcularValorTotal(10, 10);

        assertEquals(100, valor);
    }
}