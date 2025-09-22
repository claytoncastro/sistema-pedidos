package com.pedidos.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private UUID id;
    @NotBlank
    private String produto;
    @Min(1)
    private int quantidade;
    private LocalDateTime dataCriacao;
}