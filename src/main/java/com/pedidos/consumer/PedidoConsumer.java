package com.pedidos.consumer;

import com.pedidos.model.Pedido;
import com.pedidos.model.StatusPedido;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class PedidoConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "pedidos.entrada.clayton")
    public void processarPedido(Pedido pedido) throws InterruptedException {
        System.out.println("Processando pedido: " + pedido.getId());
        Thread.sleep(new Random().nextInt(2000) + 1000);

        if (Math.random() < 0.2) {
            throw new RuntimeException("Erro no processamento");
        }

        StatusPedido status = new StatusPedido(pedido.getId(), "SUCESSO", LocalDateTime.now(), null);
        rabbitTemplate.convertAndSend("pedidos.status.sucesso.clayton", status);
    }

    @RabbitListener(queues = "pedidos.entrada.clayton.dlq")
    public void processarFalha(Pedido pedido) {
        StatusPedido status = new StatusPedido(pedido.getId(), "FALHA", null, "Erro no processamento");
        rabbitTemplate.convertAndSend("pedidos.status.falha.clayton", status);
    }
}