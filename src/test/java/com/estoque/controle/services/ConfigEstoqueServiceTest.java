package com.estoque.controle.services;

import com.estoque.controle.dto.InventarioDTO;
import com.estoque.controle.exceptions.ProdutoNaoEncontradoException;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.fornecedor.TipoFornecedor;
import com.estoque.controle.model.produto.ConfigEstoque;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.StatusEstoque;
import com.estoque.controle.repository.ConfigEstoqueRepository;
import com.estoque.controle.repository.ProdutoRepository;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ConfigEstoqueServiceTest {

    @Mock
    private ConfigEstoqueRepository configEstoqueRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ConfigEstoqueService configEstoqueService;

    @Test
    @DisplayName("Deve realizar a configuração do estoque")
    void deveConfigurarOEstoque() {
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

        ConfigEstoque configEstoque = new ConfigEstoque();
        configEstoque.setId(1L);
        configEstoque.setEstoqueMinimo(5);
        configEstoque.setEstoqueMaximo(15);
        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(configEstoqueService.verificaStatus(configEstoque.getEstoqueMinimo(),configEstoque.getEstoqueMaximo(), produto.getQuantidade()));

        Mockito.when(configEstoqueRepository.save(Mockito.any(ConfigEstoque.class))).thenReturn(configEstoque);

        ConfigEstoque resultado = configEstoqueService.configurarEstoque(5, 15, 1L);

        assertNotNull(resultado.getId());
        assertEquals(configEstoque.getId(), resultado.getId());
        assertEquals(StatusEstoque.NORMAL, resultado.getStatusEstoque());
        assertEquals(configEstoque.getEstoqueMinimo(), resultado.getEstoqueMinimo());

        Mockito.verify(configEstoqueRepository, Mockito.times(1)).save(Mockito.any(ConfigEstoque.class));

    }

    @Test
    @DisplayName("Deve lançar a exceção de produto não encontrado")
    void deveLancarAExcecaoProdutoNaoEncontrado() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            configEstoqueService.configurarEstoque(5,15, 1L);
        });
    }

    @Test
    @DisplayName("Deve realizar a mudanca do status do estoque")
    void deveAtualizarOStatusDoEstoque() {
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

        ConfigEstoque configEstoque = new ConfigEstoque();
        configEstoque.setId(1L);
        configEstoque.setEstoqueMinimo(10);
        configEstoque.setEstoqueMaximo(15);
        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(StatusEstoque.NORMAL);

        Mockito.when(configEstoqueRepository.findByProdutoId(1L)).thenReturn(Optional.of(configEstoque));

        Mockito.when(configEstoqueRepository.save(Mockito.any(ConfigEstoque.class))).thenAnswer(invocation -> invocation.getArgument(0));

        configEstoqueService.atualizaStatus(1L);

        ArgumentCaptor<ConfigEstoque> captor = ArgumentCaptor.forClass(ConfigEstoque.class);
        Mockito.verify(configEstoqueRepository, Mockito.times(1)).save(captor.capture());

        ConfigEstoque resultado = captor.getValue();

        assertNotNull(resultado);
        assertNotEquals(StatusEstoque.NORMAL, resultado.getStatusEstoque());

    }

    @Test
    @DisplayName("Deve laçar uma exceção de produto não encontrado na atualização do status")
    void deveLancarAExcecaoProdutoNaoEncontrato() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> {
            configEstoqueService.atualizaStatus(1L);
        });
    }

    @Test
    @DisplayName("Verifica status do estoque")
    void deveMudarOStatusDoEstoque() {
        StatusEstoque statusEstoque = StatusEstoque.NORMAL;
        StatusEstoque testeMudaABAIXO = configEstoqueService.verificaStatus(5,10,5);
        StatusEstoque testeMudaACIMA = configEstoqueService.verificaStatus(5,10,10);
        StatusEstoque testeNaoMuda = configEstoqueService.verificaStatus(5,10,6);

        assertNotEquals(statusEstoque, testeMudaABAIXO);
        assertNotEquals(statusEstoque, testeMudaACIMA);
        assertEquals(statusEstoque, testeNaoMuda);
    }

    @Test
    @DisplayName("Deve retornar o inventario com o nome do produto, quantidade, status do estoque, estoque minimo e maximo")
    void deveRetornarOInventarioDoEstoque() {
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

        ConfigEstoque configEstoque = new ConfigEstoque();
        configEstoque.setId(1L);
        configEstoque.setEstoqueMinimo(5);
        configEstoque.setEstoqueMaximo(15);
        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(StatusEstoque.NORMAL);

        Mockito.when(configEstoqueRepository.findAll()).thenReturn(List.of(configEstoque));

        List<InventarioDTO> resultado = configEstoqueService.inventario();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        InventarioDTO dto = resultado.getFirst();
        assertEquals("Tv Samsung", dto.nomeProduto());
        assertEquals(10, dto.estoqueAtual());
        assertEquals(StatusEstoque.NORMAL, dto.statusEstoque());
        assertEquals(5, dto.estoqueMinimo());
        assertEquals(15,dto.estoqueMaximo());

        Mockito.verify(configEstoqueRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar produtos abaixo do minimo")
    void deveListarProdutosAbaixoDoMinimo() {
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

        ConfigEstoque configEstoque = new ConfigEstoque();
        configEstoque.setId(1L);
        configEstoque.setEstoqueMinimo(10);
        configEstoque.setEstoqueMaximo(15);
        configEstoque.setProduto(produto);
        configEstoque.setStatusEstoque(StatusEstoque.ABAIXO);

        Mockito.when(configEstoqueRepository.findAll()).thenReturn(List.of(configEstoque));

        List<String> resultado = configEstoqueService.produtosAbaixoDoMinimo();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst());

        Mockito.verify(configEstoqueRepository, Mockito.times(1)).findAll();
    }


}