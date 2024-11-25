package org.project;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {
    // Создаем HttpClient
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> fetchApiResponse(String url) {
        // Создаем GET-запрос и отправляем его на сервер
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Ошибка при подключении" + e.getMessage());
        }
        return null;
    }
}
