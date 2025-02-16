package com.tinkoff_lab.service.n;

import com.tinkoff_lab.dto.n.EmailDTO;

import java.util.List;

public interface EmailService {
    List<EmailDTO> getEmails();

    EmailDTO getEmail(String id);

    void deleteEmail(String id);

    EmailDTO updateEmail(EmailDTO emailDTO);

    EmailDTO addEmail(EmailDTO emailDTO);

    void verifyEmail(String code);
}
