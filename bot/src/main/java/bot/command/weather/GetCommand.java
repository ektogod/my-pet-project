package bot.command.weather;

import bot.command.Command;
import bot.external.GetHandler;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class GetCommand extends Command {
    @Autowired
    private GetHandler getHandler;
    public GetCommand() {
        super("/get", "returns list of your cities");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        TelegramRequest request = new TelegramRequest(chat.getId(), user.getUserName());
        String response = getHandler.get(request);

        sendMsg(absSender, chat.getId(), response);
    }
}
