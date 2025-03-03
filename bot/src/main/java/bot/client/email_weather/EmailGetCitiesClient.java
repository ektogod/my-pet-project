package bot.client.email_weather;

import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.entity.CityPK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface EmailGetCitiesClient {
    @GetExchange("/email/{email}/getCities")
    ResponseEntity<List<CityDTO>> getCities(@PathVariable String email, @RequestParam long chatId);
}
