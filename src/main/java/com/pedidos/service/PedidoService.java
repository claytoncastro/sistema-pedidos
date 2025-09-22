package com.pedidos.service;

import com.pedidos.dto.request.PedidoPostRequest;
import com.pedidos.dto.response.PedidoPostResponse;

import java.util.UUID;

public interface PedidoService {
    PedidoPostResponse enviarPedido(PedidoPostRequest pedido);
    String consultarStatus(UUID id);
}
