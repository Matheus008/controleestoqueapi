package com.estoque.controle.model.usuario;

public record RegisterDTO(String email, String senha, NivelDeUsuario nivelDeUsuario, String nomeUsuario) {
}
