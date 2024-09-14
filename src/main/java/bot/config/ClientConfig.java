package bot.config;

import bot.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor

public class ClientConfig {
    private final AppConfig appConfig;

    @Bean
    public TranslationClient translationClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(TranslationClient.class);
    }

    @Bean
    public SubscribeClient subscribeClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(SubscribeClient.class);
    }

    @Bean
    public UnsubscribeClient unsubscribeClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(UnsubscribeClient.class);
    }

    @Bean
    public GetClient getClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(GetClient.class);
    }

    @Bean
    public DeleteClient deleteClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(DeleteClient.class);
    }
}
