package bot.command.service;

import bot.command.Command;
import bot.states.CurrentState;
import bot.states.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends Command {
    @Autowired
    private CurrentState curState;

    public StartCommand() {
        super("/start", "starts the bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        curState.setState(States.NONE);
        String welcomeMsg = String.format("Welcome, %s! Glad to see you here!", user.getUserName());
        sendMsg(absSender, chat.getId(), welcomeMsg);
    }
}
