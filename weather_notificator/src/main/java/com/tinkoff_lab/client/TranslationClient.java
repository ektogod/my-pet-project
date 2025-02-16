package com.tinkoff_lab.client;

import com.tinkoff_lab.dto.translation.responses.TranslateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface TranslationClient {
    @GetExchange
    ResponseEntity<TranslateResponse> getTranslation(
            @RequestParam("q") String text,
            @RequestParam("langpair") String langPair);
}
