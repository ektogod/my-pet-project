package bot;

import bot.bot.BotService;
import com.tinkoff_lab.dto.weather.TelegramUserMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserNotificator {
    private final BotService botService;

    public void notifyUsers(TelegramUserMessages userMessages){
        for (int i = 0; i < userMessages.getMessages().size(); i++) {
            botService.sendMessage(userMessages.getMessages().get(i), userMessages.getChatIds().get(i));
        }
    }

}
