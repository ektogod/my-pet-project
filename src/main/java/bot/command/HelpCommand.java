package bot.command;

import bot.states.CurrentState;
import bot.states.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class HelpCommand extends Command {
    @Autowired
    private CurrentState curState;

    public HelpCommand() {
        super("/help", "command info");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        curState.setState(States.NONE);
        String infoMsg = """
                /start --> starts the bot.
                
                /help --> this command gives short explanations of other commands.
                
                /translate --> this command translates your text.
                
                /subscribe --> this command subscribes you to notification about current weather in cities.
                
                /unsubscribe --> this command removes all cities from your tracked list.
                
                /get --> this command returns you the list of your cities.
                
                /delete --> this command removes specified cities from your tracked list.""";
        sendMsg(absSender, chat.getId(), infoMsg);
    }
}
