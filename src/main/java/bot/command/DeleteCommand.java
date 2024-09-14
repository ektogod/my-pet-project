package bot.command;

import bot.CurrentState;
import bot.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class DeleteCommand extends Command{
    @Autowired
    private CurrentState currentState;


    public DeleteCommand() {
        super("/delete", "removes cities");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = "Write the cities whose weather you don't want to track anymore. Please do it in the following format:\nMinsk, Belarus\nGrodno, Belarus";
        currentState.setState(States.DELETE_CITY);
        sendMsg(absSender, chat.getId(), msg);
    }
}
