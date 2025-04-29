package com.tinkoff_lab.dto.translation;

public record Translation(Integer id,   //// record for saving info about database records
                          String username,
                          String ip,
                          String originalText,
                          String originalLang,
                          String translatedText,
                          String targetLang,
                          String time,
                          int status,
                          String message
) {
    public Translation(String username, String ip, String originalText, String originalLang, String translatedText, String targetLang, String time, int status, String message) {
        this(null, username, ip, originalText, originalLang, translatedText, targetLang, time, status, message);
    }
}
