package com.tinkoff_lab.external;

import com.tinkoff_lab.client.IPClient;
import com.tinkoff_lab.client.TranslationClient;
import com.tinkoff_lab.dto.translation.Translation;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import com.tinkoff_lab.dto.translation.responses.TranslateResponse;
import com.tinkoff_lab.exception.TranslationException;
import com.tinkoff_lab.utils.DayTimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class TranslationDefiner {
    TranslationClient translationClient;
    IPClient ipClient;
    Logger logger = LoggerFactory.getLogger(TranslationDefiner.class);

    public String getTranslation(UserRequest request){
        ResponseEntity<TranslateResponse> response = translationClient.getTranslation(
                request.text(),
                String.format("%s|%s", request.originalLanguage(), request.finalLanguage()));

        logger.info("Request has received: text = {} ", request.text());

        if (!HttpStatus.valueOf(response.getBody().getResponseStatus()).is2xxSuccessful()) {   // checking status - throwing exception if something wrong
            throwException(response, request);
        }

        logger.info("Translation successfully completed");
        return response.getBody().getTranslatedText();
    }

    private void throwException(ResponseEntity<TranslateResponse> response, UserRequest request) {
        var resultBody = response.getBody();
        Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
                ipClient.getIp().ip(),
                request.text(),
                request.originalLanguage(),
                "",
                request.finalLanguage(),
                DayTimeUtils.getMoscowTime(),
                resultBody.getResponseStatus(),
                resultBody.getResponseDetails());

        logger.error("Something goes wrong with translation for text <{}>, status is {}",
                request.text(),
                resultBody.getResponseStatus());

        throw new TranslationException(resultBody.getResponseDetails(), translation);
    }
}
