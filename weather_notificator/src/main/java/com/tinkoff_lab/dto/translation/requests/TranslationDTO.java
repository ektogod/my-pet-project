package com.tinkoff_lab.dto.translation.requests;

public record TranslationDTO(String username, String text, String originalLanguage, String finalLanguage) {   // a "body" of user request
}
