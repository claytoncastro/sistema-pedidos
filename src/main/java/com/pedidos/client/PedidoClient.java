package com.pedidos.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class PedidoClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl = "http://localhost:8080/api/pedidos";

    public UUID enviarPedido(String produto, int quantidade) throws IOException {
        Map<String, Object> payload = Map.of(
            "produto", produto,
            "quantidade", quantidade,
            "dataCriacao", LocalDateTime.now().toString()
        );

        RequestBody body = RequestBody.create(
            mapper.writeValueAsString(payload),
            MediaType.get("application/json")
        );

        Request request = new Request.Builder()
            .url(baseUrl)
            .post(body)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro: " + response.code());
            }

            JsonNode json = mapper.readTree(response.body().string());
            return UUID.fromString(json.get("id").asText());
        }
    }

    public String consultarStatus(UUID id) {
        Request request = new Request.Builder()
            .url(baseUrl + "/status/" + id)
            .get()
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) return "ERRO";
            return response.body().string().replace("\"", "");
        } catch (IOException e) {
            return "ERRO";
        }
    }
}