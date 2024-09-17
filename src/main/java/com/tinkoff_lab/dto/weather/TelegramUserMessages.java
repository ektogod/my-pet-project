package com.tinkoff_lab.dto.weather;

import com.tinkoff_lab.entity.TelegramUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TelegramUserMessages {
    List<Long> chatIds = new ArrayList<>();
    List<String> messages = new ArrayList<>();
}
