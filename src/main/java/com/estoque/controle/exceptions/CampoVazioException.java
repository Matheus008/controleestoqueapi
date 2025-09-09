package com.estoque.controle.exceptions;

public class CampoVazioException extends RuntimeException{

    public CampoVazioException() {
        super("Preencha os campos!");
    }
}
