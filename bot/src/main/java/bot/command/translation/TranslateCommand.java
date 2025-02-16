package bot.command.translation;

import bot.command.Command;
import bot.states.CurrentState;
import bot.states.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
@Component
public class TranslateCommand extends Command {
    @Autowired
    private CurrentState state;
    public TranslateCommand(CurrentState state) {
        super("/translate", "Get translation");
        this.state = state;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = "First write the text to be translated";
        state.setState(States.TRANSLATE_TEXT);
        sendMsg(absSender, chat.getId(), msg);
    }
}
