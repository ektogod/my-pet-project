package com.tinkoff_lab.service.n.impl;

import com.tinkoff_lab.dao.jpa.CityRepository;
import com.tinkoff_lab.dao.jpa.EmailRepository;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.mapper.CityMapper;
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
    CityMapper cityMapper;
    EmailRepository emailRepository;
    CityRepository cityRepository;

    @Override
    public List<EmailDTO> getEmails() {
        log.debug("Starting getEmails method.");
        var emails = mapper.entitiesToDto(emailRepository.findAll());
        log.info("Successfully retrieved emails.");
        log.debug("Leaving getEmails method.");
        return emails;
    }

    @Override
    public List<CityDTO> getEmailCities(String email) {
        log.debug("Starting getEmailCities method.");
        var cities = emailRepository.getUserCities(email);
        log.info("Successfully retrieved cities.");
        log.debug("Leaving getEmailCities method.");
        return cityMapper.entitiesToDto(cities);
    }

    @Override
    public EmailDTO getEmail(String id) {
        log.debug("Starting getEmail method with id {}.", id);
        var email = emailRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Email with id {} not found!", id);
                    return new EntityNotFoundException("Room not found!");
                });

        log.info("Successfully retrieved email with id {}.", id);
        log.debug("Leaving getEmail method.");
        return mapper.entityToDto(email);
    }

    @Override
    public void deleteEmail(String id) {
        log.debug("Starting deleteEmail method with id {}.", id);
        var email = emailRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Attempt to delete email with non-existent id {}.", id);
                    return new EntityNotFoundException("Email not found!");
                });

        emailRepository.deleteCitiesFromEmail(email.getEmail());
        emailRepository.delete(email);
        log.info("Successfully removed email with id {}.", id);
        log.debug("Leaving deleteEmail method.");
    }

    @Override
    public EmailDTO updateEmail(EmailDTO emailDTO) {
        log.debug("Starting updateEmail method with id {}.", emailDTO.email());
        var email = emailRepository
                .findById(emailDTO.email())
                .orElseThrow(() -> {
                    log.error("Attempt to update email with non-existent id {}.", emailDTO.email());
                    return new EntityNotFoundException("Email not found.");
                });

        log.debug("Updating email fields with new values.");
        email.setName(emailDTO.email());
        email.setVerified(emailDTO.isVerified());

        var updatedEmail = mapper.entityToDto(emailRepository.save(email));
        log.info("Successfully updated email with id {}.", emailDTO.email());
        log.debug("Leaving updateEmail method.");
        return updatedEmail;
    }

    @Override
    public EmailDTO addEmail(EmailDTO emailDTO) {
        log.debug("Starting addEmail method.");
        Email email = mapper.dtoToEntity(emailDTO);
        var createdEmail = mapper.entityToDto(emailRepository.save(email));
        log.info("Successfully created email with id {}.", emailDTO.email());
        log.debug("Leaving addEmail method.");
        return createdEmail;
    }

    @Override
    public void verifyEmail(String code) {
        var email = emailRepository
                .findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Email not found."));

        email.setVerified(true);
        emailRepository.save(email);
    }

    public EmailDTO addCitiesToEmail(String emailName, List<CityDTO> cityDTOS) {
        //log.debug("Starting addCityToEmail method for city {}.", cityDTOS.city());
        var email = emailRepository
                .findById(emailName)
                .orElseThrow(() -> {
                    log.error("Attempt to update emailName with non-existent id {}.", emailName);
                    return new EntityNotFoundException("Email not found.");
                });

        for(CityDTO cityDTO: cityDTOS) {
            var city = cityRepository
                    .findById(new CityPK(cityDTO.city(), cityDTO.country()))
                    .orElseGet(() -> {
                        var newCity = cityMapper.dtoToEntity(cityDTO);
                        return cityRepository.save(newCity);
                    });

            email.addCity(city);
            log.info("Successfully added emailName {} to city {}.", emailName, cityDTO.city());
        }

        emailRepository.save(email);
        log.debug("Leaving addCityToEmail method.");
        return mapper.entityToDto(email);
    }

    @Override
    public void removeCitiesFromEmail(String emailName, List<CityDTO> cityDTOS){
        //log.debug("Starting removeCityFromEmail method for city {}.", cityDTO.city());
        var email = emailRepository
                .findById(emailName)
                .orElseThrow(() -> {
                    log.error("Attempt to update email with non-existent id {}.", emailName);
                    return new EntityNotFoundException("Email not found.");
                });

        for(CityDTO cityDTO: cityDTOS) {
            var city = cityRepository
                    .findById(new CityPK(cityDTO.city(), cityDTO.country()))
                    .orElseThrow(() -> {
                        log.error("Attempt to remove city with non-existent id {}.", cityDTO.city());
                        return new EntityNotFoundException("City not found.");
                    });

            email.removeCity(city);
            //city.getEmails().remove(email);
            log.info("Successfully added email {} to city {}.", emailName, cityDTO.city());
        }

        emailRepository.save(email);
        log.debug("Leaving removeCityFromEmail method.");
    }
}
