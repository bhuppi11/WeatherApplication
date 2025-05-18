package com.zai.weather.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.zai.weather.enums.City;
import com.zai.weather.model.WeatherResponse;
import com.zai.weather.util.WeatherUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OpenWeatherMapProvider implements WeatherProvider {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(OpenWeatherMapProvider.class);
    @Value("${openweathermapprovider.url}")
    public String url;

    @Value("${openweathermapprovider.access.key}")
    private String openWeatherMapProviderApiKey;

    @Autowired
    WebClient webClient;


    @Override
    @Cacheable(value = "weather_cache", key = "#city")
    public WeatherResponse fetchWeather(String city) {
        log.info("Calling OpenWeatherMapProvider for city={}", city);

        City cityObject = City.cityMap.get(city);

        String uriString = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("lat", cityObject.getLatitude())
                .queryParam("lon", cityObject.getLongitude())
                .queryParam("appid", openWeatherMapProviderApiKey)
                .toUriString();

        JsonNode response = webClient
                .get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        double temperature = response
                .path("main")
                .path("temp")
                .asDouble();

        int windSpeed = response
                .path("wind")
                .path("speed")
                .asInt();

        return WeatherResponse.builder()
                .windSpeed(windSpeed)
                .temperatureDegrees(WeatherUtil.convertKelvinToCelsius(temperature))
                .build();
    }
}
