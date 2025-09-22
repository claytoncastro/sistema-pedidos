package com.pedidos.service;

import com.pedidos.dto.request.PedidoPostRequest;
import com.pedidos.dto.response.PedidoPostResponse;

public interface PedidoService {
    PedidoPostResponse enviarPedido(PedidoPostRequest pedido);
}
