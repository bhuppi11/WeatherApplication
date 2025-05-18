package com.zai.weather.service;

import com.zai.weather.integration.OpenWeatherMapProvider;
import com.zai.weather.integration.WeatherStackProvider;
import com.zai.weather.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final OpenWeatherMapProvider primary;
    private final WeatherStackProvider fallback;
    private final CacheManager cacheManager;

    public WeatherServiceImpl(OpenWeatherMapProvider primary, WeatherStackProvider fallback, CacheManager cacheManager) {
        this.primary = primary;
        this.fallback = fallback;
        this.cacheManager = cacheManager;
    }

    @Override
    public WeatherResponse getWeather(String city) {

        WeatherResponse weatherResponse = null;
        city = city.toLowerCase();

        Cache weatherCache = cacheManager.getCache("weather_cache");
        WeatherResponse weatherResponseFromCache = weatherCache.get(city, WeatherResponse.class);
        if (weatherResponseFromCache != null) {
            log.info("Weather info fetched from cache for city={}", city);
            return weatherResponseFromCache;
        }

        try {
            weatherResponse = primary.fetchWeather(city);;
            log.info("Weather info fetched from OpenWeatherMap provider for city={}", city);
        } catch (Exception exception) {
            log.error("Failed to fetch to fetch weather info from OpenWeatherMap provider for city={}", city);
            try {
                weatherResponse = fallback.fetchWeather(city);
                log.info("Weather info fetched from WeatherStack provider for city={}", city);
            } catch (Exception ex) {
                log.error("Failed to fetch to fetch weather info from WeatherStack provider for city={}", city);
            }
        }
        return weatherResponse;
    }
}
