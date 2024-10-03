package bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TranslationResponse(@JsonProperty("translatedText") String translatedText) {
}
