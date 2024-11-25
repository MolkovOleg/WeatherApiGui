package org.project;

import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JsonObject weatherData;

    // Настраиваем интерфейс и добавляем название
    public WeatherAppGui() {
        super("Weather App");

        // Настройка поведения программы после закрытия окна
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Настройка размера окна
        setSize(450, 650);

        // Загрузка нашего окна по центру экрана
        setLocationRelativeTo(null);

        // Сделаем менеджер компоновки пустым
        setLayout(null);

        // Уберем возможность изменения размера интерфейса
        setResizable(false);

        addGuiComponents();

    }

    private void addGuiComponents() {
        // Добавим поле поиска города
        JTextField searchField = new JTextField();

        // Установим место и размер компонентов
        searchField.setBounds(15, 15, 351, 45);

        // Изменим шрифт и размер
        searchField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchField);

        // Изображение погоды
        JLabel weatherImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherImage.setBounds(0, 125, 450, 217);
        add(weatherImage);

        // Текст температуры
        JLabel textTemperature = new JLabel("10 C");
        textTemperature.setBounds(0, 350, 450, 54);
        textTemperature.setFont(new Font("Dialog", Font.PLAIN, 48));

        // Установим текс температуры по центру
        textTemperature.setHorizontalAlignment(SwingConstants.CENTER);
        add(textTemperature);

        // Описание состояния погоды
        JLabel weatherDescription = new JLabel("Cloudy");
        weatherDescription.setBounds(0, 405, 450, 36);
        weatherDescription.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherDescription.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherDescription);

        // Изображение влажности
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        // Текст влажности
        JLabel textHumidity = new JLabel("<html><b>Humidity</b><br> 50%</html>");
        textHumidity.setBounds(90, 505, 85, 55);
        textHumidity.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(textHumidity);

        // Изображение скорости ветра
        JLabel windImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windImage.setBounds(220, 500, 74, 66);
        add(windImage);

        // Текст скорости ветра
        JLabel textWind = new JLabel("<html><b>Windspeed</b><br> 10 km/h</html>");
        textWind.setBounds(307, 505, 85, 55);
        textWind.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(textWind);

        // Кнопка поиска
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));

        // Изменим курсор при наведении на кнопку
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 15, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Получение локации от пользователя
                String userInput = searchField.getText();

                // Проверяем введенные данные
                if (userInput.replaceAll("\\s", "").isEmpty()) {
                    return;
                }

                // Извлекаем данные погоды
                weatherData = WeatherApp.getWeatherData(userInput);

                // Обновляем интерфейс
                // Обновляем изображение погоды
                String weatherCondition = weatherData.get("weatherCondition").getAsString();

                // В зависимости от состояния погоды меняем изображение
                switch (weatherCondition) {
                    case "Ясное небо" -> weatherImage.setIcon(loadImage("src/assets/clear.png"));
                    case "Пасмурно" -> weatherImage.setIcon(loadImage("src/assets/cloudy.png"));
                    case "Дождь" -> weatherImage.setIcon(loadImage("src/assets/rain.png"));
                    case "Снег" -> weatherImage.setIcon(loadImage("src/assets/snow.png"));
                }

                // Обновление текста температуры
                textTemperature.setText(weatherData.get("temperature").getAsString() + " C");

                // Обновление описания состояния погоды
                weatherDescription.setText(weatherData.get("weatherCondition").getAsString());

                // Обновление текста влажности
                textHumidity.setText("<html><b>Humidity</b><br>" + weatherData.get("humidity").getAsString() + "%</html>");

                // Обновление текста скорости ветра
                textWind.setText("<html><b>Windspeed</b><br>" + weatherData.get("windSpeed").getAsString() + " м/с</html>");
            }
        });

        // Установим кнопку по умолчанию "Enter"
        this.getRootPane().setDefaultButton(searchButton);
        add(searchButton);
    }

    // Метод загрузки изображений
    private static ImageIcon loadImage(String path) {
        try {
            // Считываем файл
            BufferedImage image = ImageIO.read(new File(path));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not load image: " + path);
        return null;
    }
}
