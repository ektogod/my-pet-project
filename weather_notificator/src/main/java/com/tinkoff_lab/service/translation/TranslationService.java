package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.dto.translation.requests.TranslationDTO;
import com.tinkoff_lab.dto.translation.responses.UserResponse;

public interface TranslationService {
    UserResponse translate(TranslationDTO request);
}
