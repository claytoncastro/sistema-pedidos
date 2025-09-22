package com.pedidos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/pedidos/status")
public class StatusController {

    private final Map<UUID, String> statusMap = new ConcurrentHashMap<>();

    @GetMapping("/{id}")
    public ResponseEntity<?> consultarStatus(@PathVariable UUID id) {
        String status = statusMap.getOrDefault(id, "DESCONHECIDO");
        return ResponseEntity.ok(Map.of("id", id, "status", status));
    }

}