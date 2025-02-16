package bot.command.weather;

import bot.command.Command;
import bot.external.UnsubscribeHandler;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class UnsubscribeCommand extends Command {
    @Autowired
    private UnsubscribeHandler unsubscribeHandler;

    public UnsubscribeCommand() {
        super("/unsubscribe", "unsubscribe from notification");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        TelegramRequest request = new TelegramRequest(chat.getId(), user.getUserName());
        String response = unsubscribeHandler.unsubscribe(request);
        sendMsg(absSender, request.chatId(), response);
    }
}
