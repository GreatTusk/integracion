package com.f776.vientosdelsur.api.proxy.openweather;

import com.f776.vientosdelsur.api.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
@AllArgsConstructor
public class OpenWeatherController {

    private final OpenWeatherService openWeatherService;

    @GetMapping
    public ResponseEntity<ApiResponse> getCurrentWeather() {
        return ResponseEntity.ok(new ApiResponse("success", openWeatherService.getCurrentWeather()));
    }
}
