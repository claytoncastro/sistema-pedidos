package com.pedidos.dto.request;

import java.time.LocalDateTime;

public record PedidoPostRequest(
    String produto,
    int quantidade,
    LocalDateTime dataCriacao
) {}