package org.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class WeatherApp {
    // Извлекаем данные о погоде из API
    public static JsonObject getWeatherData(String city) {
        // Получение данных геолокации по названию города из API
        JsonArray locationData = GeoService.getLocationData(city);
        if (locationData == null) {
            System.out.println("Не удалось найти данные города: " + city);
        }

        // Получение координат
        JsonObject location = locationData.get(0).getAsJsonObject();
        double latitude = location.get("latitude").getAsDouble();
        double longitude = location.get("longitude").getAsDouble();

        return WeatherService.getWeatherData(latitude, longitude);
    }
}
