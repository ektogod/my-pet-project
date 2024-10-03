package bot.client.weather;

import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface SubscribeClient {
    @PostExchange("/weather/telegram/subscribe")
    String response(@RequestBody WeatherTelegramRequest request);
}
