package bot;

import bot.bot.Bot;
import bot.external.DeleteHandler;
import bot.external.SubscribeHandler;
import bot.external.TranslationHandler;
import bot.external.email_weather.EmailGetHandler;
import bot.external.email_weather.EmailRegisterHandler;
import bot.external.email_weather.EmailSubscribeHandler;
import bot.states.CurrentState;
import bot.states.States;
import bot.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@Setter
@RequiredArgsConstructor

public class UpdateProcessor {
    private Bot bot;
    private final CurrentState curState;
    private final TranslationHandler translationHandler;
    private final SubscribeHandler subscribeHandler;
    private final DeleteHandler deleteHandler;
    private final EmailGetHandler emailGetHandler;
    private final EmailRegisterHandler emailRegisterHandler;
    private final EmailSubscribeHandler emailSubscribeHandler;
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
                case DELETE_CITY -> {
                    String response = deleteHandler.delete(update);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_GET -> {
                    String response = emailGetHandler.get(msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case SUBSCRIBE_EMAIL -> {
                    jedis.set("temp/email", msg);
                    curState.setState(States.SUBSCRIBE_CITIES);
                    bot.sendMessage("Now write cities you want to add.", chatId);
                }
                case SUBSCRIBE_CITIES -> {
                    emailSubscribeHandler.subscribe(jedis.get("temp/email"), msg);
                    curState.setState(States.NONE);
                    bot.sendMessage("Cities were added successfully", chatId);
                }
                case EMAIL_REGISTER -> {
                    String validCode = EmailUtils.generateValidCode();
                    jedis.set("temp/validCode", validCode);

                    String[] data = msg.split("\n");
                    jedis.set("temp/email", data[0]);
                    jedis.set("temp/emailName", data[1]);

                    emailRegisterHandler.sendValidCode(data[0], "Validation", validCode);
                    curState.setState(States.EMAIL_REGISTER_VALIDATION);
                    bot.sendMessage("Check your email for validation code. Write this code.", chatId);
                }
                case EMAIL_REGISTER_VALIDATION -> {
                    if (jedis.get("temp/validCode").equals(update.getMessage().getText())) {
                        bot.sendMessage("Validation approved!", chatId);
                        emailRegisterHandler.registerEmail(jedis.get("temp/email"), jedis.get("temp/emailName"), chatId);
                    } else {
                        bot.sendMessage("You've sent incorrect validation code", chatId);
                    }
                    curState.setState(States.NONE);
                }
                case NONE -> {
                    bot.sendMessage("Bot doesn't understand you", chatId);
                }
            }
        }
    }
}

