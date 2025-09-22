package com.pedidos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusPedido {
    private UUID idPedido;
    private String status;
    private LocalDateTime dataProcessamento;
    private String mensagemErro;
}