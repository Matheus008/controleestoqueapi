package com.estoque.controle.exceptions;

public class EstoqueInsuficienteException extends RuntimeException{

    public EstoqueInsuficienteException(String produto, int solicitado, int disponivel) {
        super("Estoque insuficiente para o produto: "+ produto +" quantidade solicitada: "+ solicitado +" quantidade disponivel: "+ disponivel);
    }
}
