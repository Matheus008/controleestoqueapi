package com.estoque.controle.services;

import com.estoque.controle.dto.relatorios.FiltroRelatorioDTO;
import com.estoque.controle.dto.relatorios.RelatorioMovimentacaoDTO;
import com.estoque.controle.dto.relatorios.RelatorioVendaDTO;
import com.estoque.controle.model.cliente.Cliente;
import com.estoque.controle.model.cliente.TipoCliente;
import com.estoque.controle.model.fornecedor.Fornecedor;
import com.estoque.controle.model.fornecedor.TipoFornecedor;
import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.produto.TipoMovimentacao;
import com.estoque.controle.model.usuario.NivelDeUsuario;
import com.estoque.controle.model.usuario.Usuario;
import com.estoque.controle.model.vendas.Venda;
import com.estoque.controle.repository.MovimentacaoRepository;
import com.estoque.controle.repository.VendaRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Deve gerar relatório entre duas datas e aplicando todos os filtros para movimentacao")
    void deveGerarRelatorioEntreDuasDatasEAplcicarTodosOsFiltroParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(5);
        movimentacao2.setDescricao("Saida estoque");

        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);

        Mockito.when(movimentacaoRepository.findByDataHoraBetween(inicio, fim))
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, fim, 1L, 1L, null, TipoMovimentacao.ENTRADA);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tv Samsung", resultado.get(0).produtoNome());
        assertEquals("admin", resultado.get(0).usuario());
        assertEquals(TipoMovimentacao.ENTRADA, resultado.get(0).tipoMovimentacao());

        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findByDataHoraBetween(inicio, fim);
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro inicio para movimentacao")
    void deveGerarRelatorioEAplcicarSomenteOFiltroInicioParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(5);
        movimentacao2.setDescricao("Saida estoque");

        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);

        Mockito.when(movimentacaoRepository.findByDataHoraAfter(inicio))
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, null, null, null, null, null);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findByDataHoraAfter(inicio);
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro fim para movimentacao")
    void deveGerarRelatorioEAplcicarSomenteOFiltroFimParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(5);
        movimentacao2.setDescricao("Saida estoque");

        LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);

        Mockito.when(movimentacaoRepository.findByDataHoraBefore(fim))
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, fim, null, null, null, null);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findByDataHoraBefore(fim);
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro produtoId para movimentacao")
    void deveGerarRelatorioEAplcicarSomenteOFiltroProdutoIdParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(10);
        movimentacao2.setDescricao("Saida estoque");


        Mockito.when(movimentacaoRepository.findAll())
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, null, 1L, null, null);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals(5, resultado.getFirst().quantidade());

        assertEquals("Tv Samsung", resultado.get(1).produtoNome());
        assertEquals(10, resultado.get(1).quantidade());

        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro tipo movimentação para movimentacao")
    void deveGerarRelatorioEAplcicarSomenteOFiltroTipoMovimentacaoParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(10);
        movimentacao2.setDescricao("Saida estoque");


        Mockito.when(movimentacaoRepository.findAll())
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, null, null, null, TipoMovimentacao.ENTRADA);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals(5, resultado.getFirst().quantidade());


        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro usuarioId para movimentacao")
    void deveGerarRelatorioEAplcicarSomenteOFiltroUsuarioIdParaMovimentacao() {

        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

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

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(1L);
        movimentacao1.setDataHora(LocalDateTime.now());
        movimentacao1.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
        movimentacao1.setProduto(produto);
        movimentacao1.setUsuario(usuario);
        movimentacao1.setQuantidade(5);
        movimentacao1.setDescricao("Entrada estoque");

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(2L);
        movimentacao2.setDataHora(LocalDateTime.now());
        movimentacao2.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao2.setProduto(produto);
        movimentacao2.setUsuario(usuario);
        movimentacao2.setQuantidade(10);
        movimentacao2.setDescricao("Saida estoque");


        Mockito.when(movimentacaoRepository.findAll())
                .thenReturn(List.of(movimentacao1, movimentacao2));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, 1L, null, null, null);

        List<RelatorioMovimentacaoDTO> resultado = relatorioService.relatorioDeMovimentacao(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals(5, resultado.getFirst().quantidade());
        assertEquals("admin", resultado.getFirst().usuario());


        Mockito.verify(movimentacaoRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Deve gerar relatório entre duas datas e aplicando todos os filtros para venda")
    void deveGerarRelatorioEntreDuasDatasEAplcicarTodosOFiltrosParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);

        Mockito.when(vendaRepository.findByDataVendaBetween(inicio, fim))
                .thenReturn(List.of(venda1));

        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, fim, 1L, 1L, 1L, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro inicio para venda")
    void deveGerarRelatorioEAplcicarSomenteOFiltrosInicioParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        Venda venda2 = new Venda();
        venda2.setId(1L);
        venda2.setQuantidade(5);
        venda2.setCliente(cliente);
        venda2.setUsuario(usuario);
        venda2.setDataVenda(LocalDateTime.now());
        venda2.setProduto(produto2);
        venda2.setValorTotalVendido((produto1.getPreco() * venda2.getQuantidade()) * 1.05);
        venda2.setMovimentacao(movimentacao);

        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);

        Mockito.when(vendaRepository.findByDataVendaAfter(inicio))
                .thenReturn(List.of(venda1, venda2));


        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(inicio, null, null, null, null, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());

        assertEquals("Tv LG", resultado.get(1).produtoNome());
        assertEquals("teste", resultado.get(1).cliente());
    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro fim para venda")
    void deveGerarRelatorioEAplcicarSomenteOFiltrosFimParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        Venda venda2 = new Venda();
        venda2.setId(1L);
        venda2.setQuantidade(5);
        venda2.setCliente(cliente);
        venda2.setUsuario(usuario);
        venda2.setDataVenda(LocalDateTime.now());
        venda2.setProduto(produto2);
        venda2.setValorTotalVendido((produto1.getPreco() * venda2.getQuantidade()) * 1.05);
        venda2.setMovimentacao(movimentacao);

        LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);

        Mockito.when(vendaRepository.findByDataVendaBefore(fim))
                .thenReturn(List.of(venda1, venda2));


        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, fim, null, null, null, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());

    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro produtoId para venda")
    void deveGerarRelatorioEAplcicarSomenteOFiltrosProdutoIdParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        Venda venda2 = new Venda();
        venda2.setId(1L);
        venda2.setQuantidade(5);
        venda2.setCliente(cliente);
        venda2.setUsuario(usuario);
        venda2.setDataVenda(LocalDateTime.now());
        venda2.setProduto(produto2);
        venda2.setValorTotalVendido((produto1.getPreco() * venda2.getQuantidade()) * 1.05);
        venda2.setMovimentacao(movimentacao);


        Mockito.when(vendaRepository.findAll())
                .thenReturn(List.of(venda1, venda2));


        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, null, 1L, null, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());

    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro clienteId para venda")
    void deveGerarRelatorioEAplcicarSomenteOFiltrosClienteIdParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        Venda venda2 = new Venda();
        venda2.setId(1L);
        venda2.setQuantidade(5);
        venda2.setCliente(cliente);
        venda2.setUsuario(usuario);
        venda2.setDataVenda(LocalDateTime.now());
        venda2.setProduto(produto2);
        venda2.setValorTotalVendido((produto1.getPreco() * venda2.getQuantidade()) * 1.05);
        venda2.setMovimentacao(movimentacao);


        Mockito.when(vendaRepository.findAll())
                .thenReturn(List.of(venda1, venda2));


        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, null, null, 1L, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());
        assertEquals("teste", resultado.get(1).cliente());

    }

    @Test
    @DisplayName("Deve gerar relatório e aplicando somente o filtro usuarioId para venda")
    void deveGerarRelatorioEAplcicarSomenteOFiltrosUsuarioIdParaVenda() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNomeCliente("teste");
        fornecedor.setCpfOuCnpj("99999999999");
        fornecedor.setTipoFornecedor(TipoFornecedor.FISICA);

        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Tv Samsung");
        produto1.setDescricao("Tv pirata");
        produto1.setPreco(1000.00);
        produto1.setQuantidade(10);
        produto1.setValorTotal(produto1.getPreco() * produto1.getQuantidade());
        produto1.setFornecedor(fornecedor);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Tv LG");
        produto2.setDescricao("Tv da xuxa");
        produto2.setPreco(1000.00);
        produto2.setQuantidade(10);
        produto2.setValorTotal(produto2.getPreco() * produto2.getQuantidade());
        produto2.setFornecedor(fornecedor);


        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNomeCliente("teste");
        cliente.setCpfOuCnpj("99999999999");
        cliente.setTipoCliente(TipoCliente.FISICA);


        Usuario usuario = new Usuario("teste@teste.com", "123", NivelDeUsuario.ADMIN, "admin");
        usuario.setId(1L);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1L);
        movimentacao.setDataHora(LocalDateTime.now());
        movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        movimentacao.setProduto(produto1);
        movimentacao.setUsuario(usuario);
        movimentacao.setQuantidade(5);
        movimentacao.setDescricao("Vendido para os chineses");

        Venda venda1 = new Venda();
        venda1.setId(1L);
        venda1.setQuantidade(5);
        venda1.setCliente(cliente);
        venda1.setUsuario(usuario);
        venda1.setDataVenda(LocalDateTime.now());
        venda1.setProduto(produto1);
        venda1.setValorTotalVendido((produto1.getPreco() * venda1.getQuantidade()) * 1.05);
        venda1.setMovimentacao(movimentacao);

        Venda venda2 = new Venda();
        venda2.setId(1L);
        venda2.setQuantidade(5);
        venda2.setCliente(cliente);
        venda2.setUsuario(usuario);
        venda2.setDataVenda(LocalDateTime.now());
        venda2.setProduto(produto2);
        venda2.setValorTotalVendido((produto1.getPreco() * venda2.getQuantidade()) * 1.05);
        venda2.setMovimentacao(movimentacao);


        Mockito.when(vendaRepository.findAll())
                .thenReturn(List.of(venda1, venda2));


        FiltroRelatorioDTO filtro = new FiltroRelatorioDTO(null, null, 1L, null, null, null);


        List<RelatorioVendaDTO> resultado = relatorioService.relatorioDeVenda(filtro);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tv Samsung", resultado.getFirst().produtoNome());
        assertEquals("teste", resultado.getFirst().cliente());
        assertEquals("teste", resultado.get(1).cliente());
        assertEquals("admin", resultado.get(1).usuario());

    }
}