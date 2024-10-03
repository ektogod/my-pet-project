package bot.client.email_weather;

import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface EmailSubscribeClient {
    @PostExchange("/weather/subscribe")
    String response(@RequestBody WeatherEmailRequest request);
}
