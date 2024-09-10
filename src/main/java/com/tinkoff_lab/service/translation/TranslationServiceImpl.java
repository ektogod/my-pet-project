package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.external.TranslationDefiner;
import com.tinkoff_lab.client.IPClient;
import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.translation.Translation;
import com.tinkoff_lab.dto.translation.requests.UserRequest;
import com.tinkoff_lab.dto.translation.responses.UserResponse;
import com.tinkoff_lab.exception.TranslationException;
import com.tinkoff_lab.service.database.TranslationDatabaseService;
import com.tinkoff_lab.utils.DayTimeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    Logger logger = LoggerFactory.getLogger(TranslationServiceImpl.class);

    AppConfig appConfig;   //for getting important data
    TranslationDatabaseService dao;     // for writing in database
    IPClient ipClient;
    TranslationDefiner translationDefiner;

    @Override
    public UserResponse translate(UserRequest request) {
        logger.info("Sending translation request: text = {} ", request.text());
        checkForNullParams(request);

        String translatedText = translationDefiner.getTranslation(request);
        dao.insert(new Translation(  // saving successful translation in database
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

    private void checkForNullParams(UserRequest request) {
        if (request.text() == null ||
                request.originalLanguage() == null ||
                request.finalLanguage() == null) {
            String message = "Translation went wrong because something from parameters is null!";
            logger.error(message);
            Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
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
