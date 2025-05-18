package com.zai.weather.controller;

import com.zai.weather.model.WeatherResponse;
import com.zai.weather.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/weather")
@Slf4j
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping
    public ResponseEntity<WeatherResponse> getWeatherInfo(@RequestParam String city) {
        log.info("Received request to fetch weather info for city={}", city);
        return ResponseEntity.ok(weatherService.getWeather(city));
    }
}
