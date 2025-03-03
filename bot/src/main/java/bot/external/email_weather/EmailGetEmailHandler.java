package bot.external.email_weather;

import bot.client.email_weather.EmailGetEmailClient;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.exception.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailGetEmailHandler {
    EmailGetEmailClient client;

    public boolean response(String email){
        boolean isExists;
        try {
            EmailDTO dto = client.getEmail(email).getBody();
            if(dto.isVerified()) isExists = true;
            else isExists = false;
        }
        catch (RestClientResponseException ex){
            isExists = false;
        }

        return isExists;
    }

}
