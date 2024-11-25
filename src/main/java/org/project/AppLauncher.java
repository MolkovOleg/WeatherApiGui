package org.project;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Отобразим наш графический интерфейс
                new WeatherAppGui().setVisible(true);
            }
        });
    }
}

