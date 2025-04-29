package com.tinkoff_lab.client;

import com.tinkoff_lab.dto.translation.responses.IPResponse;
import org.springframework.web.service.annotation.GetExchange;

public interface IPClient {
    @GetExchange
    IPResponse getIp();
}
