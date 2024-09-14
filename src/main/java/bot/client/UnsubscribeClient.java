package bot.client;

import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface UnsubscribeClient {
    @DeleteExchange("/weather/telegram/unsubscribe")
    String unsubscribe(@RequestBody TelegramRequest request);
}
