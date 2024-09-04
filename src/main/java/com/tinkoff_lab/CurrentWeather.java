package com.tinkoff_lab;

public record CurrentWeather(String city,
                             String country,
                             String weather,
                             String weatherDescription,
                             double curTemp,
                             double feelsLike,
                             double pressure,
                             double humidity,
                             double windSpeed) {
    @Override
    public String toString() {
        return String.format("Weather for %s, %s.\n%s: %s,\ncurrent temperature: %.1f°С,\nfeels like: %.1f°С,\npressure: %.0f kPa,\nhumidity: %.1f%%,\nwind speed: %.2f m/s",city, country, weather, weatherDescription, curTemp, feelsLike, pressure, humidity, windSpeed);
    }
}
