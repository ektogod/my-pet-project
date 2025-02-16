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
public class EmailRegisterCommand extends Command {
    @Autowired
    private CurrentState state;

    public EmailRegisterCommand() {
        super("/email_register", "register your email");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = "On the first line write your email and on the second one name.";
        state.setState(States.EMAIL_REGISTER);
        sendMsg(absSender, chat.getId(), msg);
    }
}
