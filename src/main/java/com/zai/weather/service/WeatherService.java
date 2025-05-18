package com.zai.weather.service;

import com.zai.weather.model.WeatherResponse;

public interface WeatherService {

    WeatherResponse getWeather(String city);
}
