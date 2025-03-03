package com.tinkoff_lab.service.n;

import com.tinkoff_lab.dto.EmailUserDTO;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailDTO;

import java.util.List;

public interface EmailService {
    List<EmailDTO> getEmails();

    List<CityDTO> getEmailCities(String email, long chatId);

    EmailDTO getEmail(String id);

    void deleteEmail(String id, long chatId);

    EmailDTO updateEmail(EmailDTO emailDTO);

    EmailDTO addEmail(EmailDTO emailDTO);

    void verifyEmail(String code, long chatId);

    EmailDTO addCitiesToEmail(String email, long chatId, List<CityDTO> cityDTOS);

    void removeCitiesFromEmail(String email, long chatId, List<CityDTO> cityDTOS);
}