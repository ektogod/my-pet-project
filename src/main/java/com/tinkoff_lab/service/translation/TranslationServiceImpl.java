package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.dto.Translation;
import com.tinkoff_lab.exception.TranslationException;
import com.tinkoff_lab.dto.requests.UserRequest;
import com.tinkoff_lab.dto.responses.TranslateResponse;
import com.tinkoff_lab.dto.responses.UserResponse;
import com.tinkoff_lab.service.database.TranslationDatabaseService;
import com.tinkoff_lab.utils.TranslationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TranslationServiceImpl implements TranslationService {
    private final Logger logger = LoggerFactory.getLogger(TranslationServiceImpl.class);

    private final AppConfig appConfig;   //for getting important data
    private final TranslationDatabaseService dao;     // for writing in database
    private final TranslationUtils utils;

    @Autowired
    public TranslationServiceImpl(AppConfig appConfig, TranslationDatabaseService dao, TranslationUtils utils) {
        this.appConfig = appConfig;
        this.dao = dao;
        this.utils = utils;
    }

    @Override
    public UserResponse translate(UserRequest request) {
        logger.info("Sending translation request: text = {} ", request.text());
        checkForNullParams(request);

        ResponseEntity<TranslateResponse> response = getResponse(request);
        logger.info("Request has received: text = {} ", request.text());

        if (!HttpStatus.valueOf(response.getBody().getResponseStatus()).is2xxSuccessful()) {   // checking status - throwing exception if something wrong
            throwException(response, request);
        }

        logger.info("Translation successfully completed");
        String translatedText = response.getBody().getTranslatedText();
        dao.insert(new Translation(  // saving in database successful translation
                utils.getIP(),
                request.text(),
                request.originalLanguage(),
                translatedText,
                request.finalLanguage(),
                utils.getMoscowTime(),
                200,
                "Ok"));
        return new UserResponse(translatedText);
    }

    private ResponseEntity<TranslateResponse> getResponse(UserRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        String url = String.format(
                appConfig.getTranslationURL(),
                request.text(),
                request.originalLanguage(),
                request.finalLanguage());

        return restTemplate.exchange( // getting a response from translation api
                url,
                HttpMethod.POST,
                httpEntity,
                TranslateResponse.class);
    }

    private void checkForNullParams(UserRequest request) {
        if (request.text() == null ||
                request.originalLanguage() == null ||
                request.finalLanguage() == null) {
            String message = "Translation went wrong because something from parameters is null!";
            logger.error(message);
            Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
                    utils.getIP(),
                    request.text(),
                    request.originalLanguage(),
                    "",
                    request.finalLanguage(),
                    utils.getMoscowTime(),
                    500,
                    message);
            throw new TranslationException(message, translation);
        }
    }

    private void throwException(ResponseEntity<TranslateResponse> response, UserRequest request) {
        var resultBody = response.getBody();
        Translation translation = new Translation(  // needs for writing in db in ExceptionHandler class
                utils.getIP(),
                request.text(),
                request.originalLanguage(),
                "",
                request.finalLanguage(),
                utils.getMoscowTime(),
                resultBody.getResponseStatus(),
                resultBody.getResponseDetails());

        logger.error("Something goes wrong with translation for text <{}>, status is {}",
                request.text(),
                resultBody.getResponseStatus());

        throw new TranslationException(resultBody.getResponseDetails(), translation);
    }
}
