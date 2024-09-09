package com.tinkoff_lab.config;

import com.tinkoff_lab.client.CoordinateClient;
import com.tinkoff_lab.client.WeatherClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class RestConfig {
    private final AppConfig config;

    @Bean
    public CoordinateClient coordinateRestTemplate() {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getOpenWeatherUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(CoordinateClient.class);
    }

    @Bean
    public WeatherClient curWeatherRestTemplate() {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getOpenWeatherUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(WeatherClient.class);
    }
}
