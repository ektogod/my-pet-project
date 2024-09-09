package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.dto.translation.requests.UserRequest;
import com.tinkoff_lab.dto.translation.responses.UserResponse;

public interface TranslationService {
    UserResponse translate(UserRequest request);
}
