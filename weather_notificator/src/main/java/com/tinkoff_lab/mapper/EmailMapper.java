package com.tinkoff_lab.mapper;

import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.entity.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Mapping(target = "isVerified", source = "isVerified")
    Email dtoToEntity(EmailDTO emailDTO);

    @Mapping(target = "isVerified", source = "isVerified")
    EmailDTO entityToDto(Email email);

    List<EmailDTO> entitiesToDto(List<Email> emails);

}
