package bot.config;

import bot.CurrentState;
import bot.States;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:bot.properties")
@Getter

public class BotConfig {
    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

//    @Bean
//    public CurrentState currentState(){
//        CurrentState state = new CurrentState();
//        state.setState(States.NONE);
//        return state;
//    }
}
