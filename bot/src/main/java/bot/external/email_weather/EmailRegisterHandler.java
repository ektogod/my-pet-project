package bot.external.email_weather;

import bot.client.EmailClient;
import bot.client.email_weather.EmailRegisterClient;
import com.tinkoff_lab.dto.EmailDTO;
import com.tinkoff_lab.dto.EmailUserRequest;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class EmailRegisterHandler {
    private final EmailClient emailClient;
    private final EmailRegisterClient registerClient;
    private String validCode;

    public void sendValidCode(String email, String subject, String validCode){
        String msg = "Hey, here is your validation code!\n\n" + validCode;
        EmailDTO emailDTO = new EmailDTO(email, msg, subject);
        emailClient.sendEmail(emailDTO);
    }

    public String registerEmail(String email, String name, long chatId){
        EmailUserRequest request = new EmailUserRequest(email, name, chatId);
        String response;
        try {
            response = registerClient.response(request);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }

        return response;
    }
}
