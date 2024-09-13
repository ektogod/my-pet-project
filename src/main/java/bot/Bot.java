package bot;

import bot.command.HelpCommand;
import bot.command.TranslateCommand;
import bot.config.BotConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UpdateProcessor processor;
    private final BotConfig config;
    private String name;
    private String token;

//    @Autowired
//    public Bot(UpdateProcessor processor, BotConfig config) {
//        this.processor = processor;
//        this.config = config;
//        this.name = config.getBotName();
//        this.token = config.getBotToken();
//        this.processor.setBot(this);
//    }

    @PostConstruct
    private void init() {
        register(translateCommand);
        register(helpCommand);

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
