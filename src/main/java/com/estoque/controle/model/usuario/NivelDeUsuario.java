package com.estoque.controle.model.usuario;

public enum NivelDeUsuario {
    ADMIN("admin"),
    GERENTE("gerente"),
    USUARIO("usuario"),
    ESTOQUE("estoque");

    private String role;

    NivelDeUsuario(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
