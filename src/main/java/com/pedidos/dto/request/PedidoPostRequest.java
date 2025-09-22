package com.pedidos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record PedidoPostRequest(
        @NotBlank(message = "O campo 'produto' não pode ser vazio ou nulo")
        String produto,
        @Min(value = 1, message = "A 'quantidade' deve ser maior que zero")
        int quantidade,
        LocalDateTime dataCriacao
) {
}