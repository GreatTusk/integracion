package com.f776.vientosdelsur.api.proxy.openweather;

import java.util.List;

public record WeatherResponse(List<WeatherType> weather, WeatherSummary main) {
}
