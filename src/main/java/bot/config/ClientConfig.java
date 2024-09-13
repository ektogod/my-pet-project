package bot.config;

import bot.client.TranslationClient;
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

}
