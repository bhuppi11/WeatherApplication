package com.zai.weather.util;


public class WeatherUtil {

    public static int convertKelvinToCelsius(double kelvin) {
        return (int) (kelvin - 273.15);
    }
}
