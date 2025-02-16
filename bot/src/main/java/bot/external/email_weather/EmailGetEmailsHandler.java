package bot.external.email_weather;

import bot.client.email_weather.EmailGetEmailsClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailGetEmailsHandler {
    EmailGetEmailsClient client;

    public String getEmails(long chatId){
        List<String> emails = client.getEmails(chatId).getBody();
        String response;
        if(emails != null && emails.isEmpty()){
            response = "You have no any emails.";
        }
        else {
            response = "Here is your emails!\n" + String.join("\n", emails);
        }

        return response;
    }
}
