package bot.client.translation;

import bot.dto.TranslationResponse;
import com.tinkoff_lab.dto.translation.requests.TranslationDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface TranslationClient {
    @PostExchange("/translate")
    TranslationResponse getTranslation(@RequestBody TranslationDTO request);
}
