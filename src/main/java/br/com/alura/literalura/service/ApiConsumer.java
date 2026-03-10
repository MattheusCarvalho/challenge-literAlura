package br.com.alura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiConsumer {

    private final HttpClient client = HttpClient.newHttpClient();

    public String getData(String url) {
              HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .build();
              HttpResponse<String> response;
              try {
                            response = client.send(request, HttpResponse.BodyHandlers.ofString());
              } catch (IOException | InterruptedException e) {
                            throw new RuntimeException("Erro ao realizar requisicao a API: " + e.getMessage(), e);
              }
              return response.body();
    }
}
