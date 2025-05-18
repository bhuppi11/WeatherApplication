package com.zai.weather.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.zai.weather.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class WeatherStackProvider implements WeatherProvider {

    @Value("${weatherstackprovider.url}")
    public String url;

    @Value("${weatherstackprovider.api.key}")
    private String weatherStackProviderApiKey;

    @Autowired
    WebClient webClient;

    @Override
    @Cacheable(value = "weather_cache", key = "#city")
    public WeatherResponse fetchWeather(String city) {

        log.info("Calling WeatherStackProvider for city={}", city);

        String uriString = UriComponentsBuilder.
                fromUriString(url)
                .queryParam("access_key", weatherStackProviderApiKey)
                .queryParam("query", city)
                .toUriString();

        JsonNode jsonNode = webClient
                .get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        int temperature = jsonNode
                .get("current")
                .get("temperature")
                .asInt();

        int windSpeed = jsonNode
                .get("current")
                .get("wind_speed")
                .asInt();

        return WeatherResponse.builder()
                .windSpeed(windSpeed)
                .temperatureDegrees(temperature)
                .build();
    }
}
