package bot;

import bot.command.*;
import bot.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
