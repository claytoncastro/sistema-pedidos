package com.pedidos.controller;

import com.pedidos.dto.request.PedidoPostRequest;
import com.pedidos.dto.response.PedidoPostResponse;
import com.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoPostResponse> criarPedido(@Valid @RequestBody PedidoPostRequest pedido) {
        var pedidoResult = pedidoService.enviarPedido(pedido);

        return ResponseEntity
                .accepted()
                .body(pedidoResult);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> consultarStatus(@PathVariable UUID id) {
        String status = pedidoService.consultarStatus(id);
        return ResponseEntity.ok(status);
    }
}