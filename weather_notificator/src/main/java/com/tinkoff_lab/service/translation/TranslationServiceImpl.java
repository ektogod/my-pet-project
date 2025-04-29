package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.client.IPClient;
import com.tinkoff_lab.dto.translation.Translation;
import com.tinkoff_lab.dto.translation.requests.TranslationDTO;
import com.tinkoff_lab.dto.translation.responses.UserResponse;
import com.tinkoff_lab.exception.TranslationException;
import com.tinkoff_lab.external.TranslationDefiner;
import com.tinkoff_lab.service.database.TranslationDatabaseService;
import com.tinkoff_lab.utils.DayTimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    TranslationDatabaseService dao;     // for writing in database
    IPClient ipClient;
    TranslationDefiner translationDefiner;

    @Override
    public UserResponse translate(TranslationDTO request) {
        log.info("Sending translation request: text = {} ", request.text());
        checkForNullParams(request);

        String translatedText = translationDefiner.getTranslation(request);
        dao.insert(new Translation(  // saving successful translation in database
                request.username(),
                ipClient.getIp().ip(),
                request.text(),
                request.originalLanguage(),
                translatedText,
                request.finalLanguage(),
                DayTimeUtils.getMoscowTime(),
                200,
                "Ok"));
        return new UserResponse(translatedText);
    }

    private void checkForNullParams(TranslationDTO request) {
        if (request.text() == null ||
                request.originalLanguage() == null ||
                request.finalLanguage() == null) {
            String message = "Translation went wrong because something from parameters is null!";
            log.error(message);
            Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
                    request.username(),
                    ipClient.getIp().ip(),
                    request.text(),
                    request.originalLanguage(),
                    "",
                    request.finalLanguage(),
                    DayTimeUtils.getMoscowTime(),
                    500,
                    message);
            throw new TranslationException(message, translation);
        }
    }
}
