package bot.client;

import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface DeleteClient {
    @DeleteExchange("/weather/telegram/delete")
    String delete(@RequestBody TelegramCitiesRequest request);
}
