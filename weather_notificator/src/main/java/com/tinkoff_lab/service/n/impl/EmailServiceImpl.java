package com.tinkoff_lab.service.n.impl;

import com.tinkoff_lab.dao.jpa.EmailRepository;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.mapper.EmailMapper;
import com.tinkoff_lab.service.n.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    EmailMapper mapper;
    EmailRepository repository;

    @Override
    public List<EmailDTO> getEmails() {
        log.debug("Starting getRooms method.");
        var emails = mapper.entitiesToDto(repository.findAll());
        log.info("Successfully retrieved emails.");
        log.debug("Leaving getRooms method.");
        return emails;
    }

    @Override
    public EmailDTO getEmail(String id) {
        log.debug("Starting getRoom method with id {}.", id);
        var email = repository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Room with id {} not found!", id);
                    return new EntityNotFoundException("Room not found!");
                });

        log.info("Successfully retrieved room with id {}.", id);
        log.debug("Leaving getRoom method.");
        return mapper.entityToDto(email);
    }

    @Override
    public void deleteEmail(String id) {
        log.debug("Starting deleteRoom method with id {}.", id);
        var email = repository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Attempt to delete email with non-existent id {}.", id);
                    return new EntityNotFoundException("Room not found!");
                });

        repository.delete(email);
        log.info("Successfully removed email with id {}.", id);
        log.debug("Leaving deleteRoom method.");
    }

    @Override
    public EmailDTO updateEmail(EmailDTO emailDTO) {
        log.debug("Starting updateRoom method with id {}.", emailDTO.email());
        var email = repository
                .findById(emailDTO.email())
                .orElseThrow(() -> {
                    log.error("Attempt to update room with non-existent id {}.", emailDTO.email());
                    return new EntityNotFoundException("Room not found.");
                });

        log.debug("Updating email fields with new values.");
        email.setName(emailDTO.email());
        email.setVerified(emailDTO.isVerified());

        var updatedEmail = mapper.entityToDto(repository.save(email));
        log.info("Successfully updated room with id {}.", emailDTO.email());
        log.debug("Leaving updateRoom method.");
        return updatedEmail;
    }

    @Override
    public EmailDTO addEmail(EmailDTO emailDTO) {
        log.debug("Starting addRoom method.");
        Email email = mapper.dtoToEntity(emailDTO);
        var createdEmail = mapper.entityToDto(repository.save(email));
        log.info("Successfully created email with id {}.", emailDTO.email());
        log.debug("Leaving addRoom method.");
        return createdEmail;
    }

    @Override
    public void verifyEmail(String code) {
        var email = repository
                .findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Email not found."));

        email.setVerified(true);
        repository.save(email);
    }
}
