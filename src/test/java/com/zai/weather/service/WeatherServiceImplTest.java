package com.zai.weather.service;

import com.zai.weather.integration.OpenWeatherMapProvider;
import com.zai.weather.integration.WeatherStackProvider;
import com.zai.weather.model.WeatherResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @InjectMocks
    WeatherServiceImpl weatherService;

    @Mock
    CacheManager cacheManager;

    @Mock
    Cache cache;

    @Mock
    OpenWeatherMapProvider openWeatherMapProvider;

    @Mock
    WeatherStackProvider weatherStackProvider;

    @Test
    void testGetWeatherDataFromCache_success() {
        String city = "melbourne";
        WeatherResponse cachedResponse = WeatherResponse.builder()
                .temperatureDegrees(25)
                .windSpeed(10)
                .build();

        Mockito.when(cacheManager.getCache("weather_cache")).thenReturn(cache);
        Mockito.when(cache.get(Mockito.eq(city), Mockito.eq(WeatherResponse.class))).thenReturn(cachedResponse);

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(cachedResponse.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(cachedResponse.getWindSpeed(), result.getWindSpeed());
    }

    @Test
    void testGetWeatherDataFromOpenWeatherMap_success() {
        String city = "melbourne";
        WeatherResponse weatherResponse = WeatherResponse.builder()
                .temperatureDegrees(25)
                .windSpeed(10)
                .build();

        Mockito.when(cacheManager.getCache("weather_cache")).thenReturn(cache);
        Mockito.when(cache.get(Mockito.eq(city), Mockito.eq(WeatherResponse.class))).thenReturn(null);
        Mockito.when(openWeatherMapProvider.fetchWeather(city)).thenReturn(weatherResponse);

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(weatherResponse.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(weatherResponse.getWindSpeed(), result.getWindSpeed());
    }

    @Test
    void testGetWeatherDataFromOpenWeatherMap_throwsException() {
        String city = "melbourne";
        WeatherResponse weatherResponse = WeatherResponse.builder()
                .temperatureDegrees(25)
                .windSpeed(10)
                .build();

        Mockito.when(cacheManager.getCache("weather_cache")).thenReturn(cache);
        Mockito.when(cache.get(Mockito.eq(city), Mockito.eq(WeatherResponse.class))).thenReturn(null);
        Mockito.when(openWeatherMapProvider.fetchWeather(city)).thenThrow(new RuntimeException("Failed to fetch data from OpenWeatherMap"));
        Mockito.when(weatherStackProvider.fetchWeather(city)).thenReturn(weatherResponse);

        WeatherResponse result = weatherService.getWeather(city);

        assertEquals(weatherResponse.getTemperatureDegrees(), result.getTemperatureDegrees());
        assertEquals(weatherResponse.getWindSpeed(), result.getWindSpeed());
    }

    @Test
    void testGetWeatherDataFromWeatherStack_throwsException() {
        String city = "melbourne";

        Mockito.when(cacheManager.getCache("weather_cache")).thenReturn(cache);
        Mockito.when(cache.get(Mockito.eq(city), Mockito.eq(WeatherResponse.class))).thenReturn(null);
        Mockito.when(openWeatherMapProvider.fetchWeather(city)).thenThrow(new RuntimeException("Failed to fetch data from OpenWeatherMap"));
        Mockito.when(weatherStackProvider.fetchWeather(city)).thenThrow(new RuntimeException("Failed to fetch data from WeatherStackProvider"));

        WeatherResponse result = weatherService.getWeather(city);

        assertNull(result);
    }

}
