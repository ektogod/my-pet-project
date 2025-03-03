package bot.config;

import bot.client.EmailClient;
import bot.client.IPClient;
import bot.client.email_weather.*;
import bot.client.translation.TranslationClient;
import bot.client.weather.DeleteClient;
import bot.client.weather.GetClient;
import bot.client.weather.SubscribeClient;
import bot.client.weather.UnsubscribeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    public EmailClient emailClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getEmailUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailClient.class);
    }

    @Bean
    public EmailGetCitiesClient emailGetClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailGetCitiesClient.class);
    }

    @Bean
    public EmailSubscribeClient emailSubscribeClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailSubscribeClient.class);
    }

    @Bean
    public EmailRegisterClient emailRegisterClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailRegisterClient.class);
    }

    @Bean
    public EmailUnsubscribeClient emailUnsubscribeClientClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailUnsubscribeClient.class);
    }

    @Bean
    public EmailGetEmailsClient emailGetEmailsClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailGetEmailsClient.class);
    }

    @Bean
    public EmailDeleteCitiesClient emailDeleteCitiesClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailDeleteCitiesClient.class);
    }

    @Bean
    public EmailGetEmailClient emailGetEmailClient() {
        RestClient client = RestClient
                .builder()
                .baseUrl(appConfig.getBotBaseUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();

        return factory.createClient(EmailGetEmailClient.class);
    }

    @Bean
    public IPClient IpClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(appConfig.getIpUrl())
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(IPClient.class);
    }
}
