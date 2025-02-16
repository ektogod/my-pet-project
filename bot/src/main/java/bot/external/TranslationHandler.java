package bot.external;

import bot.client.translation.TranslationClient;
import bot.dto.ErrorResponse;
import bot.dto.TranslationResponse;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
@Getter
@Setter

public class TranslationHandler {
    private String curOrigLang;
    private String curTargetLang;
    private String curText;
    private final TranslationClient client;

    public String getTranslation() {
        UserRequest request = new UserRequest(curText, curOrigLang, curTargetLang);
        try {
            TranslationResponse response = client.getTranslation(request);
            return response.translatedText();
        }
        catch (RestClientResponseException ex) {
            String errorMsg = ex.getResponseBodyAs(ErrorResponse.class).errorMessage();
            if (errorMsg == null) {
                errorMsg = "An error occurred during translation.";
            }
            return errorMsg;
        }
    }
}
