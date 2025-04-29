package bot.external.email_weather;

import bot.client.EmailClient;
import bot.client.IPClient;
import bot.client.email_weather.EmailRegisterClient;
import bot.dto.IPResponse;
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
    private final IPClient ipClient;
    private String validCode;

    public void sendMessage(String link, String email, long chatId, String subject, String validCode){
        IPResponse response = ipClient.getIp();
        String msg = "Hey, here is your validation link!\n\n" + link + "/auth/verify?code=" + validCode + "&chatId="+ chatId + "\n\nThe password for page inside: " + response.ip();
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
