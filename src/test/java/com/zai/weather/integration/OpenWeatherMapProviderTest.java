package com.zai.weather.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zai.weather.model.WeatherResponse;
import com.zai.weather.util.WeatherUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherMapProviderTest {

    @InjectMocks
    private OpenWeatherMapProvider openWeatherMapProvider;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    String jsonString;
    String city;
    int temp;
    int windSpeed;

    @BeforeEach
    public void setup() {
        temp = 290;
        windSpeed = 1;
        openWeatherMapProvider.url = "temp-url.cpm";
        city = "melbourne";
        jsonString = String.format("{ " +
                "\"main\" : \n" +
                "      {\n" +
                "         \"temp\": %d\n" +
                "      },\n" +
                "\"wind\" : \n" +
                "      {\n" +
                "         \"speed\": %d\n" +
                "      }\n" +
                "}", temp, windSpeed);

    }

    @Test
    void testFetchWeather() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);

        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<JsonNode>>notNull())).thenReturn(Mono.just(jsonNode));

        WeatherResponse response = openWeatherMapProvider.fetchWeather(city);

        assertEquals(response.getWindSpeed(), windSpeed);
        assertEquals(response.getTemperatureDegrees(), WeatherUtil.convertKelvinToCelsius(temp));
    }
}
