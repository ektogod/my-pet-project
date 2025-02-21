package bot;

import bot.bot.Bot;
import bot.external.*;
import bot.external.email_weather.EmailGetCitiesHandler;
import bot.external.email_weather.EmailRegisterHandler;
import bot.external.email_weather.EmailSubscribeHandler;
import bot.external.email_weather.EmailUnsubscribeHandler;
import bot.states.CurrentState;
import bot.states.States;
import bot.utils.EmailUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@Setter
@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateProcessor {
    private Bot bot;
    private final CurrentState curState;
    private final TranslationHandler translationHandler;
    private final SubscribeHandler subscribeHandler;
    private final UnsubscribeHandler unsubscribeHandler;
    private final GetHandler getHandler;
    private final DeleteHandler deleteHandler;
    private final EmailGetCitiesHandler emailGetCitiesHandler;
    private final EmailRegisterHandler emailRegisterHandler;
    private final EmailSubscribeHandler emailSubscribeHandler;
    private final EmailUnsubscribeHandler emailUnsubscribeHandler;
    private final JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);


    public void handleUpdate(Update update) {
        long chatId = update.getMessage().getChatId();
        String msg = update.getMessage().getText();
        try (Jedis jedis = jedisPool.getResource()) {
            switch (curState.getState()) {
                case TRANSLATE_TEXT -> {
                    translationHandler.setCurText(msg);
                    curState.setState(States.TRANSLATE_LANGS);
                    bot.sendMessage("Write original and target languages. Do it in the following format:\nru be", chatId);
                }
                case TRANSLATE_LANGS -> {
                    String[] data = msg.split(" +");
                    translationHandler.setCurOrigLang(data[0]);
                    translationHandler.setCurTargetLang(data[1]);
                    curState.setState(States.NONE);
                    bot.sendMessage(translationHandler.getTranslation(), chatId);
                }
                case SUBSCRIBE -> {
                    String response = subscribeHandler.subscribe(update);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case UNSUBSCRIBE -> {
                    String response = unsubscribeHandler.unsubscribe(chatId);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case GET_CITIES -> {
                    String response = getHandler.get(chatId);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case DELETE_CITY -> {
                    String response = deleteHandler.delete(update);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_GET_CITIES -> {
                    String response = emailGetCitiesHandler.get(msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case SUBSCRIBE_EMAIL -> {
                    jedis.set("temp/email", msg);
                    curState.setState(States.SUBSCRIBE_CITIES);
                    bot.sendMessage("Now write cities you want to add.", chatId);
                }
                case SUBSCRIBE_CITIES -> {
                    String response = emailSubscribeHandler.subscribe(jedis.get("temp/email"), msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_REGISTER -> {
                    String validCode = EmailUtils.generateValidCode();

                    String[] data = msg.split("\n");
                    jedis.set("temp/email", data[0]);
                    jedis.set("temp/emailName", data[1]);

                    emailRegisterHandler.sendMessage(data[0], "Validation", validCode);
                    emailRegisterHandler.registerEmail(data[0], data[1], chatId, validCode);

                    curState.setState(States.NONE);
                    bot.sendMessage("Check your email for validation link.", chatId);
                }
                case EMAIL_UNSUBSCRIBE -> {
                    String response = emailUnsubscribeHandler.unsubscribe(msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case NONE -> bot.sendMessage("Bot doesn't understand you", chatId);
            }
        }
    }
}

