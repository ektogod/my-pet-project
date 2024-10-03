package com.tinkoff_lab.config;

import com.tinkoff_lab.client.*;
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
public class ClientConfig {
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

    @Bean
    public TranslationClient translationRestTemplate() {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getTranslationURL())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(TranslationClient.class);
    }

    @Bean
    public IPClient IpRestTemplate() {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getIpUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(IPClient.class);
    }

    @Bean
    public TelegramClient telegramClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8070/ektogod")
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(TelegramClient.class);
    }

    @Bean
    public EmailClient EmailClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(config.getEmailUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(EmailClient.class);
    }
}
