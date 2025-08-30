package com.estoque.controle.model.vendas;

import com.estoque.controle.model.cliente.Cliente;
import com.estoque.controle.model.produto.Movimentacao;
import com.estoque.controle.model.produto.Produto;
import com.estoque.controle.model.usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vendas_tb")
public class Venda {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "usuario_id")
    @ManyToOne
    private Usuario usuario;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "valor_total_vendido")
    private double valorTotalVendido;

    @JoinColumn(name = "produto_id")
    @ManyToOne
    private Produto produto;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movimentacao_id")
    private Movimentacao movimentacao;

    @JoinColumn(name = "cliente_id")
    @ManyToOne
    private Cliente cliente;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda;

    @PrePersist
    public void prePesrsist() {
        this.dataVenda = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorTotalVendido() {
        return valorTotalVendido;
    }

    public void setValorTotalVendido(double valorTotalVendido) {
        this.valorTotalVendido = valorTotalVendido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }
}
