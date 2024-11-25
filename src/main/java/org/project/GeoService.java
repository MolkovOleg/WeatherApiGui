package org.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class GeoService {
    public static JsonArray getLocationData(String city) {
        // Заменяем все пропуски в названиях городов на
        city = city.replaceAll(" ", "+");

        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                city + "&count=1&language=ru&format=json";

        var apiResponse = HttpUtils.fetchApiResponse(urlString);
        if (apiResponse == null || apiResponse.statusCode() != 200) {
            System.out.println("Ошибка: невозможно подключиться к API");
            return null;
        }

        // Парсинг полученных данных
        try {
            var jsonGeoData = JsonParser.parseString(apiResponse.body()).getAsJsonObject();
            if (!jsonGeoData.has("results")
                    || jsonGeoData.getAsJsonArray("results").isEmpty()) {
                System.out.println("Город не найден!");
                return null;
            }
            return jsonGeoData.getAsJsonArray("results");
        } catch (Exception e) {
            System.out.println("Ошибка обработка данных геолокации: " + e.getMessage());
        }
        // Не может найти локацию
        return null;
    }
}
