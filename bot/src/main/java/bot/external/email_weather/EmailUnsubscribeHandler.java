package bot.external.email_weather;

import bot.client.email_weather.EmailUnsubscribeClient;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailUnsubscribeHandler {
    EmailUnsubscribeClient client;

    public String unsubscribe(String msg){
        String response;
        try{
            response = client.unsubscribe(msg);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }

        return response;
    }
}
