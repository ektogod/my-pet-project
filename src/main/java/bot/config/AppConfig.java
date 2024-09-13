package bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:bot.properties")
@Getter

public class AppConfig {
    @Value("${bot.baseUrl}")
    private String botBaseUrl;
}
