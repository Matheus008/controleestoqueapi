package com.estoque.controle.dto;

import com.estoque.controle.model.usuario.NivelDeUsuario;

public record RegisterDTO(String email, String senha, NivelDeUsuario nivelDeUsuario, String nomeUsuario) {
}
