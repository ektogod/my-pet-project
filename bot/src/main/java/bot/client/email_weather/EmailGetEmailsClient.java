package bot.client.email_weather;

import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.entity.CityPK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface EmailGetEmailsClient {
    @GetExchange("/weather/get/{chatId}")
    ResponseEntity<List<String>> getEmails(@PathVariable long chatId);
}
