package bot.client.email_weather;

import com.tinkoff_lab.dto.EmailUserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailRegisterClient {
    @PostExchange("/weather/register")
    String response(@RequestBody EmailUserRequest request);
}
