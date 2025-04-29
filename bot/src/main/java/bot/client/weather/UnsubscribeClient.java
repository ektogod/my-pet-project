package bot.client.weather;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;

public interface UnsubscribeClient {
    @DeleteExchange("/user/{id}")
    String unsubscribe(@PathVariable long id);
}
