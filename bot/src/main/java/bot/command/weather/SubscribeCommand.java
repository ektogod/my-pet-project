package bot.command.weather;

import bot.command.Command;
import bot.states.CurrentState;
import bot.states.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class SubscribeCommand extends Command {
    @Autowired
    private CurrentState state;
    public SubscribeCommand() {
        super("/subscribe", "subscribes tg user to weather notification");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        state.setState(States.SUBSCRIBE);
        String msg = "Write the cities whose weather you want to track. Please do it in the following format:\nMinsk, Belarus\nGrodno, Belarus";
        sendMsg(absSender, chat.getId(), msg);
    }
}
