package bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class HelpCommand extends Command {
    public HelpCommand() {
        super("/help", "command info");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String infoMsg =
                "/help  --> this command gives short explanations about other commands.\n" +
                "/translate --> this command translates your text.";
        sendMsg(absSender, chat.getId(), infoMsg);
    }
}
