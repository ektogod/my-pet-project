package bot;

import bot.bot.Bot;
import bot.external.*;
import bot.external.email_weather.*;
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

import java.io.IOException;

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
    private final EmailDeleteCitiesHandler emailDeleteCitiesHandler;
    private final EmailGetEmailHandler emailGetEmailHandler;
    private final EmailRegisterHandler emailRegisterHandler;
    private final EmailSubscribeHandler emailSubscribeHandler;
    private final EmailUnsubscribeHandler emailUnsubscribeHandler;
    private final UrlGetter getter;
    private boolean isLinkGot = false;
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
                    curState.setState(States.NONE);
                    bot.sendMessage(translationHandler.getTranslation(update.getMessage().getChat().getUserName(), data[0], data[1]), chatId);
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
                    String response = emailGetCitiesHandler.get(msg, chatId);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_SUBSCRIBE_CITIES_EMAIL -> {
                    jedis.set("temp/email", msg);
                    boolean response = emailGetEmailHandler.response(msg);
                    if(response){
                        curState.setState(States.EMAIL_SUBSCRIBE_CITIES);
                        bot.sendMessage("Now write cities you want to add.", chatId);
                    }
                    else {
                        curState.setState(States.NONE);
                        bot.sendMessage("You have no such verified email!", chatId);
                    }
                }
                case EMAIL_SUBSCRIBE_CITIES -> {
                    String response = emailSubscribeHandler.subscribe(jedis.get("temp/email"), chatId, msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_REGISTER -> {
                    String validCode = EmailUtils.generateValidCode();

                    String[] data = msg.split("\n");
                    try {
                        if (!isLinkGot) {
                            String url = getter.getUrl();
                            jedis.set("temp/curUrl", url);
                            isLinkGot = true;
                        }

                        String response = emailRegisterHandler.registerEmail(data[0], data[1], chatId, validCode);
                        emailRegisterHandler.sendMessage(jedis.get("temp/curUrl"), data[0], chatId,"Validation", validCode);

                        curState.setState(States.NONE);
                        if(response.isBlank()) bot.sendMessage("Check your email for validation link.", chatId);
                        else bot.sendMessage(response, chatId);
                    } catch (IOException e) {
                        bot.sendMessage("Something went wrong with creating confirming link", chatId);
                    }
                }
                case EMAIL_UNSUBSCRIBE -> {
                    String response = emailUnsubscribeHandler.unsubscribe(msg, chatId);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }
                case EMAIL_DELETE_CITIES_EMAIL -> {
                    jedis.set("temp/email", msg);
                    boolean response = emailGetEmailHandler.response(msg);
                    if(response){
                        curState.setState(States.EMAIL_DELETE_CITIES);
                        bot.sendMessage("Now write cities you want to delete.", chatId);
                    }
                    else {
                        curState.setState(States.NONE);
                        bot.sendMessage("You have no such verified email!", chatId);
                    }
                }
                case EMAIL_DELETE_CITIES -> {
                    String response = emailDeleteCitiesHandler.deleteCities(jedis.get("temp/email"), chatId, msg);
                    curState.setState(States.NONE);
                    bot.sendMessage(response, chatId);
                }

                case NONE -> bot.sendMessage("Bot doesn't understand you", chatId);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            bot.sendMessage("Something wrong with amount of parameters! Please check your request and try again!", chatId);
        }
    }
}

