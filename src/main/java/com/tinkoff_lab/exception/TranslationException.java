package com.tinkoff_lab.exception;

import com.tinkoff_lab.dto.Translation;
import lombok.Getter;

@Getter
public class TranslationException extends RuntimeException{  //exception in case of troubles with text translation
    private final Translation translation;
    public TranslationException(String message, Translation translation) {
        super(message);
        this.translation = translation;
    }
}
