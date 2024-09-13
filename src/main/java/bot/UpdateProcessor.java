package bot;

import bot.client.TranslationClient;
import bot.dto.TranslationResponse;
import bot.external.TranslationHandler;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Setter
@RequiredArgsConstructor

public class UpdateProcessor {
    private Bot bot;
    private final CurrentState curState;
    private final TranslationHandler translationHandler;

    public void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        switch (curState.getState()) {
            case TRANSLATE_LANGS -> {
                String[] data = update.getMessage().getText().split(" +");
                translationHandler.setCurOrigLang(data[0]);
                translationHandler.setCurTargetLang(data[1]);
                curState.setState(States.TRANSLATE_TEXT);
                bot.sendMessage("Write text to translate", chatId);
            }
            case TRANSLATE_TEXT -> {
                translationHandler.setCurText(update.getMessage().getText());
                curState.setState(States.NONE);
                bot.sendMessage(translationHandler.getTranslation(), chatId);
            }
            case NONE -> {
                bot.sendMessage("Bot doesn't understand you", chatId);
            }
        }
    }
}

