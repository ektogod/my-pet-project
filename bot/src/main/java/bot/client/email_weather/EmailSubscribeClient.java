package bot.client.email_weather;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailCitiesDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface EmailSubscribeClient {
    @PostExchange("/email/{email}/addCities")
    String response(@PathVariable String email, @RequestBody List<CityDTO> cityDTOS, @RequestParam long chatId);
}
