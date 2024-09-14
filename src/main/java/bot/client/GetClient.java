package bot.client;

import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import com.tinkoff_lab.entity.CityPK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface GetClient {
    @GetExchange("/weather/telegram/get")
    ResponseEntity<List<CityPK>> getCities(@RequestBody TelegramRequest request);
}
