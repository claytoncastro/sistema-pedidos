package com.pedidos.service.impl;

import com.pedidos.dto.PedidoStatusMemory;
import com.pedidos.dto.request.PedidoPostRequest;
import com.pedidos.dto.response.PedidoPostResponse;
import com.pedidos.model.Pedido;
import com.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoStatusMemory statusMemory;

    @Value("${rabbitmq.queue.pedidos}")
    private String pedidosQueue;

    @Override
    public PedidoPostResponse enviarPedido(PedidoPostRequest request) {
        var pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .produto(request.produto())
                .quantidade(request.quantidade())
                .dataCriacao(LocalDateTime.now())
                .build();

        statusMemory.atualizarStatus(pedido.getId(), "RECEBIDO");
        rabbitTemplate.convertAndSend(pedidosQueue, pedido);

        return PedidoPostResponse.toDTO(pedido);
    }

    @Override
    public String consultarStatus(UUID id) {
        return statusMemory.consultarStatus(id);
    }

}