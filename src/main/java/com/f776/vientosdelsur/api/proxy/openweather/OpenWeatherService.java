package com.f776.vientosdelsur.api.proxy.openweather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenWeatherService {

    private final WebClient webClient;

    @Value("${spring.keys.open-weather-api-key}")
    private String openWeatherApiKey;


    public OpenWeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openweathermap.org")
                .build();
    }

    public WeatherResponse getCurrentWeather() {
        final float PUCON_LAT = -39.266667f;
        final float PUCON_LON = -71.966667f;

        return webClient
                .get()
                .uri("/data/2.5/weather?lat={PUCON_LAT}&lon={PUCON_LON}&units=metric&appid={openWeatherApiKey}&lang=es",
                        PUCON_LAT, PUCON_LON, openWeatherApiKey)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new RuntimeException("API call failed")))
                .bodyToMono(WeatherResponse.class)
                .block();
    }

}
