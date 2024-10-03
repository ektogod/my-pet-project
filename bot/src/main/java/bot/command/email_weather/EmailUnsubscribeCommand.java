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
public class EmailUnsubscribeCommand extends Command {
    @Autowired
    private CurrentState state;

    public EmailUnsubscribeCommand() {
        super("/email_unsubscribe", "unsubscribes email from notification");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = "Write your email.";
        state.setState(States.EMAIL_UNSUBSCRIBE);
        sendMsg(absSender, chat.getId(), msg);
    }
}
