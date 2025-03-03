package bot.client.email_weather;

import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;

public interface EmailUnsubscribeClient {
    @DeleteExchange("/email/{id}/delete")
    String unsubscribe(@PathVariable String id, @RequestParam("chatId") long chatId);
}
