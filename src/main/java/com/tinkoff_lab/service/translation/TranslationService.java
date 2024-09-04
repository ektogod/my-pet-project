package com.tinkoff_lab.service.translation;

import com.tinkoff_lab.dto.requests.UserRequest;
import com.tinkoff_lab.dto.responses.UserResponse;

public interface TranslationService {
    UserResponse translate(UserRequest request);
}
