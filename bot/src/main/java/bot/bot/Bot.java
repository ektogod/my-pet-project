package bot.bot;

import bot.UpdateProcessor;
import bot.command.email_weather.EmailGetCommand;
import bot.command.email_weather.EmailRegisterCommand;
import bot.command.email_weather.EmailSubscribeCommand;
import bot.command.service.HelpCommand;
import bot.command.service.StartCommand;
import bot.command.translation.TranslateCommand;
import bot.command.weather.DeleteCommand;
import bot.command.weather.GetCommand;
import bot.command.weather.SubscribeCommand;
import bot.command.weather.UnsubscribeCommand;
import bot.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingCommandBot {
    private final TranslateCommand translateCommand;
    private final HelpCommand helpCommand;
    private final StartCommand startCommand;
    private final SubscribeCommand subscribeCommand;
    private final GetCommand getCommand;
    private final UnsubscribeCommand unsubscribeCommand;
    private final DeleteCommand deleteCommand;
    private final EmailRegisterCommand emailRegisterCommand;
    private final EmailSubscribeCommand emailSubscribeCommand;
    private final EmailGetCommand emailGetCommand;

    private final UpdateProcessor processor;
    private final BotConfig config;
    private String name;
    private String token;

    @PostConstruct
    private void init() {
        register(translateCommand);
        register(helpCommand);
        register(startCommand);
        register(subscribeCommand);
        register(unsubscribeCommand);
        register(getCommand);
        register(deleteCommand);
        register(emailRegisterCommand);
        register(emailSubscribeCommand);
        register(emailGetCommand);

        this.name = config.getBotName();
        this.token = config.getBotToken();
        this.processor.setBot(this);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            processor.handleUpdate(update);
        }
    }

    public void sendMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
