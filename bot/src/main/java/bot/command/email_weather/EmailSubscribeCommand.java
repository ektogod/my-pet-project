package bot.command.email_weather;

import bot.command.Command;
import bot.states.CurrentState;
import bot.states.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class EmailSubscribeCommand extends Command {
    @Autowired
    private CurrentState state;

    public EmailSubscribeCommand() {
        super("/email_subscribe", "adds city to your email");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = "Firstly write email to which you want to add new cities for tracking.";
        state.setState(States.SUBSCRIBE_EMAIL);
        sendMsg(absSender, chat.getId(), msg);
    }
}
