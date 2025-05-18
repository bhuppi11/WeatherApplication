package com.zai.weather.integration;

import com.zai.weather.model.WeatherResponse;

public interface WeatherProvider {

    WeatherResponse fetchWeather(String city);
}
