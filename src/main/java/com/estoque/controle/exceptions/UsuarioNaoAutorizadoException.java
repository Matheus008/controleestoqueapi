package com.estoque.controle.exceptions;

public class UsuarioNaoAutorizadoException extends  RuntimeException{

    public UsuarioNaoAutorizadoException() {
        super("O usuário não tem permissão para essa funcionalidade!");
    }
}
