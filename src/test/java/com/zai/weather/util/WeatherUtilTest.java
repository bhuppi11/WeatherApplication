package com.zai.weather.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherUtilTest {

    @Test
    void testConvertKelvinToCelsius() {
        double temperatureInKelvin = 300.0;
        int expectedTemperatureCelsius = 26;
        int actualCelsius = WeatherUtil.convertKelvinToCelsius(temperatureInKelvin);

        assertEquals(expectedTemperatureCelsius, actualCelsius);
    }


}
