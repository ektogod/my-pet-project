package bot.client.weather;

import com.tinkoff_lab.dto.n.UserCitiesDTO;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface DeleteClient {
    @DeleteExchange("/user/deleteCities")
    String delete(@RequestBody UserCitiesDTO request);
}
