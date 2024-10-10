package com.f776.vientosdelsur.api.proxy.openweather;

public record WeatherSummary(
        Double temp,
        Double feels_like,
        Double temp_min,
        Double temp_max,
        Integer pressure,
        Integer humidity,
        Integer sea_level,
        Integer grnd_level
) {
}