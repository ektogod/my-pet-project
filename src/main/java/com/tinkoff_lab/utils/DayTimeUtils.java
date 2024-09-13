package com.tinkoff_lab.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DayTimeUtils {
    public static String getMoscowTime() {  // getting the Moscow time of the request
        ZonedDateTime now = ZonedDateTime
                .now()
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return now.format(dateTimeFormatter);
    }
}
