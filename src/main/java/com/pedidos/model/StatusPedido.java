package com.pedidos.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StatusPedido {
    private UUID idPedido;
    private String status;
    private LocalDateTime dataProcessamento;
    private String mensagemErro;
}