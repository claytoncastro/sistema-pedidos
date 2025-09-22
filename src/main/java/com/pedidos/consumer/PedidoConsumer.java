package com.pedidos.consumer;

import com.pedidos.dto.PedidoStatusMemory;
import com.pedidos.model.Pedido;
import com.pedidos.model.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PedidoConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoStatusMemory statusMemory;

    @RabbitListener(queues = "pedidos.entrada.clayton")
    public void processarPedido(Pedido pedido) throws InterruptedException {
        statusMemory.atualizarStatus(pedido.getId(), "PROCESSANDO");
        System.out.println("Processando pedido: " + pedido.getId());
        Thread.sleep(new Random().nextInt(2000) + 1000);

        if (Math.random() < 0.2) {
            throw new RuntimeException("Erro no processamento");
        }

        statusMemory.atualizarStatus(pedido.getId(), "SUCESSO");

        var status = StatusPedido.builder()
                .idPedido(pedido.getId())
                .status("SUCESSO")
                .dataProcessamento(LocalDateTime.now())
                .mensagemErro(null)
                .build();
        rabbitTemplate.convertAndSend("pedidos.status.sucesso.clayton", status);
    }

    @RabbitListener(queues = "pedidos.entrada.clayton.dlq")
    public void processarFalha(Pedido pedido) {
        statusMemory.atualizarStatus(pedido.getId(), "FALHA");

        var status = StatusPedido.builder()
                .idPedido(pedido.getId())
                .status("FALHA")
                .dataProcessamento(LocalDateTime.now())
                .mensagemErro("Erro no processamento")
                .build();
        rabbitTemplate.convertAndSend("pedidos.status.falha.clayton", status);
    }
}