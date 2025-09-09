package com.estoque.controle.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(LocalDateTime dateTime, int status,String error, String message) {
}
