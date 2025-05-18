package com.zai.weather.model;

import lombok.Data;
import lombok.Builder;

@Builder
@Data
public class WeatherResponse {

    private int windSpeed;
    private int temperatureDegrees;
}
