package com.pedidos.dto.response;

import com.pedidos.model.Pedido;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PedidoPostResponse(
        UUID id,
        String produto,
        int quantidade,
        LocalDateTime dataCriacao
) {
    public static PedidoPostResponse toDTO(Pedido pedido) {
        return PedidoPostResponse.builder()
                .id(pedido.getId())
                .produto(pedido.getProduto())
                .quantidade(pedido.getQuantidade())
                .dataCriacao(pedido.getDataCriacao())
                .build();
    }
}