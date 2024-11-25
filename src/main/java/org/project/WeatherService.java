package org.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherService {
    public static JsonObject getWeatherData(double latitude, double longitude) {
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="
                + latitude + "&longitude="
                + longitude
                + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&wind_speed_unit=ms&timezone=Europe%2FMoscow";

        var apiResponse = HttpUtils.fetchApiResponse(urlString);
        if (apiResponse == null || apiResponse.statusCode() != 200) {
            System.out.println("Ошибка: невозможно подключиться к API");
            return null;
        }

        // Парсинг полученных данных
        try {
            var jsonWeatherData = JsonParser.parseString(apiResponse.body()).getAsJsonObject();
            if (!jsonWeatherData.has("hourly")
                    || jsonWeatherData.getAsJsonObject("hourly").isEmpty()) {
                System.out.println("Ошибка с данными города");
            }

            // Извлечение часовых данных
            JsonObject hourly = jsonWeatherData.getAsJsonObject("hourly");

            // Извлечение времени
            // Также нам нужно найти индекс текущего времени
            JsonArray time = hourly.getAsJsonArray("time");
            int index = findIndexOfCurrentTime(time);

            // Получение температуры по индексу текущего времени
            JsonArray temperatureDate = hourly.getAsJsonArray("temperature_2m");
            double temperature = temperatureDate.get(index).getAsDouble();

            // Получение кода погоды по индексу текущего времени
            JsonArray weatherCode = hourly.getAsJsonArray("weather_code");
            String weatherCondition = convertWeatherCode(weatherCode.get(index).getAsInt());

            // Получение влажности по индексу текущего времени
            JsonArray relativeHumidity = hourly.getAsJsonArray("relative_humidity_2m");
            double humidity = relativeHumidity.get(index).getAsDouble();

            // Получение скорости ветра по индексу текущего времени
            JsonArray windSpeed = hourly.getAsJsonArray("wind_speed_10m");
            double windSpeedValue = windSpeed.get(index).getAsDouble();

            // Формирование JSON-объекта с данными погоды
            JsonObject weatherData = new JsonObject();
            weatherData.addProperty("temperature", temperature);
            weatherData.addProperty("humidity", humidity);
            weatherData.addProperty("windSpeed", windSpeedValue);
            weatherData.addProperty("weatherCondition", weatherCondition);

            return weatherData;

        } catch (Exception e) {
            System.out.println("Ошибка обработка данных погоды: " + e.getMessage());
        }
        return null;
    }

    private static int findIndexOfCurrentTime(JsonArray time) {
        String currentTime = getCurrentTime();

        // Итерируемся по часовому списку и сравниваем с текущим временем
        for (int i = 0; i < time.size(); i++) {
            String timeValue = time.get(i).getAsString();
            if (timeValue.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Форматируем дату и время в вид 2024-11-25T12:00:00 (как в API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return currentDateTime.format(formatter);
    }

    // Метод для конвертации кода погоды
    private static String convertWeatherCode(int weatherCode) {
        String weatherCondition = "";
        if (weatherCode == 0) {
            weatherCondition = "Ясное небо";
        } else if (weatherCode >= 1 && weatherCode <= 3) {
            weatherCondition = "Пасмурно";
        } else if ((weatherCode >= 51 && weatherCode <= 67)
                || (weatherCode >= 80 && weatherCode <= 99)) {
            weatherCondition = "Дождь";
        } else if (weatherCode >= 71 && weatherCode <= 77) {
            weatherCondition = "Снег";
        }
        return weatherCondition;
    }
}
