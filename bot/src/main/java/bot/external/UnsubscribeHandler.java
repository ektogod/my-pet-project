package bot.external;

import bot.client.weather.UnsubscribeClient;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class UnsubscribeHandler {
    private final UnsubscribeClient client;

    public String unsubscribe(long chatId){
        String response;
        try{
            response = client.unsubscribe(chatId);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }

        return response;
    }
}
