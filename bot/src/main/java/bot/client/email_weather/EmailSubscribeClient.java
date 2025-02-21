package bot.client.email_weather;

import com.tinkoff_lab.dto.n.EmailCitiesDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailSubscribeClient {
    @PostExchange("/email/addCities")
    String response(@RequestBody EmailCitiesDto request);
}
