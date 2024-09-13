package bot.external;

import bot.client.TranslationClient;
import bot.dto.TranslationResponse;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter

public class TranslationHandler {
    private String curOrigLang;
    private String curTargetLang;
    private String curText;
    private final TranslationClient client;

    public String getTranslation(){
        UserRequest request = new UserRequest(curText, curOrigLang, curTargetLang);
        TranslationResponse response = client.getTranslation(request);
        return response.translatedText();
    }
}
