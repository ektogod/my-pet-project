package bot.client.translation;

import bot.dto.TranslationResponse;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TranslationClient {
    @PostExchange("/translateText")
    TranslationResponse getTranslation(@RequestBody UserRequest request);
}
