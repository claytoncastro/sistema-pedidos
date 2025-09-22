package com.pedidos.dto;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PedidoStatusMemory {
    private final Map<UUID, String> statusMap = new ConcurrentHashMap<>();

    public void atualizarStatus(UUID id, String status) {
        statusMap.put(id, status);
    }

    public String consultarStatus(UUID id) {
        return statusMap.getOrDefault(id, "DESCONHECIDO");
    }
}