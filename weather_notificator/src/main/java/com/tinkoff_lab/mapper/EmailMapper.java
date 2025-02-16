package com.tinkoff_lab.mapper;

import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.entity.Email;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    Email dtoToEntity(EmailDTO emailDTO);

    EmailDTO entityToDto(Email email);

    List<EmailDTO> entitiesToDto(List<Email> emails);

}
