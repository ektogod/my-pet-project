package com.tinkoff_lab.config;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:application.properties")
@Configuration
@Getter
public class AppConfig {                  // I use this class to have access to application.properties data
    @Value("${translation.url}")
    private String translationURL;

    @Value("${spring.datasource.url}")
    private String databaseURL;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${ip.url}")
    private String ipUrl;

    @Value("${openWeather.url}")
    private String openWeatherUrl;

    @Value("${openWeather.key}")
    private String openWeatherKey;

    @Value("${email.url}")
    private String emailUrl;
}
