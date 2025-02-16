package bot.command.email_weather;

import bot.command.Command;
import bot.external.email_weather.EmailGetEmailsHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class EmailGetEmailsCommand extends Command {
    @Autowired
    private EmailGetEmailsHandler handler;
    public EmailGetEmailsCommand() {
        super("/email_get_emails", "returns user emails");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String response = handler.getEmails(chat.getId());
        sendMsg(absSender, chat.getId(), response);
    }
}
