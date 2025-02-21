package com.tinkoff_lab.service.n.impl;

import com.tinkoff_lab.dao.jpa.CityRepository;
import com.tinkoff_lab.dao.jpa.EmailRepository;
import com.tinkoff_lab.dao.jpa.UserRepository;
import com.tinkoff_lab.dto.n.CityDTO;
import com.tinkoff_lab.dto.n.EmailDTO;
import com.tinkoff_lab.dto.n.UserDTO;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.mapper.CityMapper;
import com.tinkoff_lab.mapper.EmailMapper;
import com.tinkoff_lab.mapper.UserMapper;
import com.tinkoff_lab.service.n.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class UserServiceImpl implements UserService {
    EmailMapper mapper;
    CityMapper cityMapper;
    UserMapper userMapper;
    EmailRepository emailRepository;
    CityRepository cityRepository;
    UserRepository userRepository;

    @Override
    public List<UserDTO> getUsers() {
        log.debug("Starting getUsers method.");
        var users = userMapper.entitiesToDto(userRepository.findAll());
        log.info("Successfully retrieved users.");
        log.debug("Leaving getUsers method.");
        return users;
    }

    @Override
    public UserDTO getUser(long id) {
        log.debug("Starting getUser method with id {}.", id);
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("User with id {} not found!", id);
                    return new EntityNotFoundException("User not found!");
                });

        log.info("Successfully retrieved user with id {}.", id);
        log.debug("Leaving getUser method.");
        return userMapper.entityToDto(user);
    }

    public List<CityDTO> getUserCities(long chatId){
        log.debug("Starting getUserCities method with id {}.", chatId);
        var cities = userRepository
                .getUserCities(chatId);

        log.info("Successfully retrieved cities with id {}.", chatId);
        log.debug("Leaving getUserCities method.");
        return cityMapper.entitiesToDto(cities);
    }

    @Override
    public void deleteUser(long id) {
        log.debug("Starting deleteUser method with id {}.", id);
        var user = userRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.error("Attempt to delete user with non-existent id {}.", id);
                    return new EntityNotFoundException("Room not found!");
                });

        userRepository.deleteCitiesFromUser(id);
        userRepository.deleteEmailsFromUser(id);
        userRepository.delete(user);

        log.info("Successfully removed user with id {}.", id);
        log.debug("Leaving deleteUser method.");
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        log.debug("Starting updateUser method with id {}.", userDTO.chatId());
        var user = userRepository
                .findById(userDTO.chatId())
                .orElseThrow(() -> {
                    log.error("Attempt to update user with non-existent id {}.", userDTO.chatId());
                    return new EntityNotFoundException("User not found.");
                });

        log.debug("Updating user fields with new values.");
        user.setChatId(userDTO.chatId());
        user.setUsername(userDTO.username());
        user.setLastname(userDTO.lastname());
        user.setFirstname(userDTO.firstname());

        var updatedEmail = userMapper.entityToDto(userRepository.save(user));
        log.info("Successfully updated user with id {}.", userDTO.chatId());
        log.debug("Leaving updateUser method.");
        return updatedEmail;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        log.debug("Starting addUser method.");
        User user = userMapper.dtoToEntity(userDTO);
        var createdUser = userMapper.entityToDto(userRepository.save(user));
        log.info("Successfully created user with id {}.", userDTO.chatId());
        log.debug("Leaving addUser method.");
        return createdUser;
    }

    @Override
    public UserDTO addCitiesToUser(long chatId, List<CityDTO> cityDTOS) {
        //log.debug("Starting addCityToEmail method for city {}.", cityDTOS.city());
        var user = userRepository
                .findById(chatId)
                .orElseThrow(() -> {
                    log.error("Attempt to update user with non-existent id {}.", chatId);
                    return new EntityNotFoundException("User not found.");
                });

        for(CityDTO cityDTO: cityDTOS) {
            var city = cityRepository
                    .findById(new CityPK(cityDTO.city(), cityDTO.country()))
                    .orElseGet(() -> {
                        var newCity = cityMapper.dtoToEntity(cityDTO);
                        return cityRepository.save(newCity);
                    });

            user.addCity(city);
            log.info("Successfully added emailName {} to city {}.", chatId, cityDTO.city());
        }

        userRepository.save(user);
        log.debug("Leaving addCitiesToUser method.");
        return userMapper.entityToDto(user);
    }

    @Override
    public void removeCitiesFromUser(long chatId, List<CityDTO> cityDTOS){
        //log.debug("Starting removeCityFromEmail method for city {}.", cityDTO.city());
        var user = userRepository
                .findById(chatId)
                .orElseThrow(() -> {
                    log.error("Attempt to update user with non-existent id {}.", chatId);
                    return new EntityNotFoundException("User not found.");
                });

        for(CityDTO cityDTO: cityDTOS) {
            var city = cityRepository
                    .findById(new CityPK(cityDTO.city(), cityDTO.country()))
                    .orElseThrow(() -> {
                        log.error("Attempt to remove city with non-existent id {}.", cityDTO.city());
                        return new EntityNotFoundException("City not found.");
                    });

            user.removeCity(city);
            //city.getEmails().remove(user);
            log.info("Successfully added user {} to city {}.", chatId, cityDTO.city());
        }

        userRepository.save(user);
        log.debug("Leaving removeCityFromEmail method.");
    }

    @Override
    public UserDTO addEmailsToUser(long chatId, List<EmailDTO> emailDTOS) {
        var user = userRepository
                .findById(chatId)
                .orElseThrow(() -> {
                    log.error("Attempt to update user with non-existent id {}.", chatId);
                    return new EntityNotFoundException("User not found.");
                });

        for(EmailDTO emailDTO: emailDTOS) {
            var email = emailRepository
                    .findById(emailDTO.email())
                    .orElseGet(() -> {
                        var newEmail = mapper.dtoToEntity(emailDTO);
                        return emailRepository.save(newEmail);
                    });

            user.addEmail(email);
            log.info("Successfully added city {} to user {}.", chatId, emailDTO.email());
        }

        userRepository.save(user);
        log.debug("Leaving addEmailsToUser method.");
        return userMapper.entityToDto(user);
    }

    @Override
    public void removeEmailsFromUser(long chatId, List<EmailDTO> emailDTOS) {
        var user = userRepository
                .findById(chatId)
                .orElseThrow(() -> {
                    log.error("Attempt to update user with non-existent id {}.", chatId);
                    return new EntityNotFoundException("User not found.");
                });

        for(EmailDTO emailDTO: emailDTOS) {
            var email = emailRepository
                    .findById(emailDTO.email())
                    .orElseThrow(() -> {
                        log.error("Attempt to remove email with non-existent id {}.", emailDTO.email());
                        return new EntityNotFoundException("City not found.");
                    });

            user.removeEmail(email);
            //email.getEmails().remove(user);
            log.info("Successfully removed email {} from user {}.", emailDTO.email(), chatId);
        }

        userRepository.save(user);
        log.debug("Leaving removeEmailsFromUser method.");
    }
}
