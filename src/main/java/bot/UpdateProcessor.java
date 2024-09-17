package bot;

import bot.bot.Bot;
import bot.external.DeleteHandler;
import bot.external.SubscribeHandler;
import bot.external.TranslationHandler;
import bot.states.CurrentState;
import bot.states.States;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Setter
@RequiredArgsConstructor

public class UpdateProcessor {
    private Bot bot;
    private final CurrentState curState;
    private final TranslationHandler translationHandler;
    private final SubscribeHandler subscribeHandler;
    private final DeleteHandler deleteHandler;

    public void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        switch (curState.getState()) {
            case TRANSLATE_TEXT -> {
                translationHandler.setCurText(update.getMessage().getText());
                curState.setState(States.TRANSLATE_LANGS);
                bot.sendMessage("Write original and target languages. Do it in the following format:\nru be", chatId);
            }
            case TRANSLATE_LANGS -> {
                String[] data = update.getMessage().getText().split(" +");
                translationHandler.setCurOrigLang(data[0]);
                translationHandler.setCurTargetLang(data[1]);
                curState.setState(States.NONE);
                bot.sendMessage(translationHandler.getTranslation(), chatId);
            }
            case SUBSCRIBE -> {
                String response = subscribeHandler.subscribe(update);
                curState.setState(States.NONE);
                bot.sendMessage(response, update.getMessage().getChatId());
            }
            case DELETE_CITY -> {
                String response = deleteHandler.delete(update);
                curState.setState(States.NONE);
                bot.sendMessage(response, update.getMessage().getChatId());
            }
            case NONE -> {
                bot.sendMessage("Bot doesn't understand you", chatId);
            }
        }
    }
}

