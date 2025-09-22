package com.pedidos.controller;

import com.pedidos.dto.request.PedidoPostRequest;
import com.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@Valid @RequestBody PedidoPostRequest pedido) {
        var pedidoResult = pedidoService.enviarPedido(pedido);
        return ResponseEntity.accepted().body(Map.of("id", pedidoResult.id()));
    }
}