package com.estoque.controle.model.produto;

import jakarta.persistence.*;

@Entity
@Table(name = "config_estoque_tb")
public class ConfigEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "estoque_minimo")
    private int estoqueMinimo;

    @Column(name = "estoque_maximo")
    private int estoqueMaximo;

    @OneToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "status")
    private StatusEstoque statusEstoque;

    public ConfigEstoque() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueMaximo() {
        return estoqueMaximo;
    }

    public void setEstoqueMaximo(int estoqueMaximo) {
        this.estoqueMaximo = estoqueMaximo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public StatusEstoque getStatusEstoque() {
        return statusEstoque;
    }

    public void setStatusEstoque(StatusEstoque statusEstoque) {
        this.statusEstoque = statusEstoque;
    }
}
