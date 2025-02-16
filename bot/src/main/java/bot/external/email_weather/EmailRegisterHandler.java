package bot.external.email_weather;

import bot.client.EmailClient;
import bot.client.email_weather.EmailRegisterClient;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.dto.SendEmailDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class EmailRegisterHandler {
    private final EmailClient emailClient;
    private final EmailRegisterClient registerClient;
    private String validCode;

    public void sendMessage(String email, String subject, String validCode){
        String msg = "Hey, here is your validation link!\n\nhttp://localhost:8080/auth/verify?code=" + validCode;
        SendEmailDTO sendEmailDTO = new SendEmailDTO(email, msg, subject);
        emailClient.sendEmail(sendEmailDTO);
    }

    public String registerEmail(String email, String name, long chatId, String code){
        EmailDTO emailDTO = new EmailDTO(email, name, code, false);
        String response;
        try {
            response = registerClient.response(emailDTO);
        }
        catch (RestClientResponseException ex){
            response = ex.getResponseBodyAs(String.class);
        }

        return response;
    }
}
