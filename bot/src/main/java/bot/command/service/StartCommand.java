package bot.command.service;

import bot.client.weather.SubscribeClient;
import bot.command.Command;
import bot.states.CurrentState;
import bot.states.States;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;

@Component
public class StartCommand extends Command {
    @Autowired
    private CurrentState curState;
    @Autowired
    private SubscribeClient client;

    public StartCommand() {
        super("/start", "starts the bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        client.response(new WeatherTelegramRequest(chat.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), new ArrayList<>()));//subscribing user
        curState.setState(States.NONE);
        String welcomeMsg = String.format("Welcome, %s! Glad to see you here!", user.getUserName());
        sendMsg(absSender, chat.getId(), welcomeMsg);
    }
}
