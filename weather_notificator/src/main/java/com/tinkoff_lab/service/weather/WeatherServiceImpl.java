package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dao.hibernate.*;
import com.tinkoff_lab.dto.EmailUserRequest;
import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.dto.weather.CityDTO;
import com.tinkoff_lab.dto.weather.Coordinates;
import com.tinkoff_lab.dto.weather.request.email.EmailCitiesRequest;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.external.CoordinatesDefiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class WeatherServiceImpl implements WeatherService {
    EmailDAO emailDAO;
    CityDAO cityDAO;
    UserDAO userDAO;
    EmailCityDAO emailCityDAO;
    UserEmailDAO userEmailDAO;
    Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    CoordinatesDefiner definer;

    @Override
    public void register(EmailUserRequest request) {
        logger.info("Start adding user with email {} and chatId {} to database", request.email(), request.chatId());
        Email email = new Email(request.email(), request.name());
        if (emailDAO.findByID(email.getEmail()) != null) {
            logger.warn("User with email {} already exists!", email.getEmail());
            throw new WrongWeatherRequestException(String.format("User with email %s already exists!", email));
        }

        emailDAO.insert(email);
        userEmailDAO.addUserEmail(userDAO.findByID(request.chatId()), email);
    }

    @Override
    public void add(WeatherEmailRequest request) {
        logger.info("Start adding user with email {} and cities {} to database", request.email(), request.cities());
        Email email = emailDAO.findByID(request.email());
        if (email == null) {
            logger.warn("User with email {} doesn't exist!", request.email());
            throw new WrongWeatherRequestException(String.format("User with email %s doesn't exist!", request.email()));
        }

        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            emailCityDAO.addEmailCity(email, city);
            //user.getCities().add(city);
        }

        logger.info("Adding user with email {} and cities {} to database ended successfully", email.getEmail(), email.getCities());
    }

    @Override
    public void deleteUser(EmailRequest request) {
        logger.info("Start deleting user with email {}", request);
        Email email = emailDAO.findByID(request.email());
        if (email == null) {
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        Set<City> cities = new HashSet<>(email.getCities());
        for (City city : cities) {
            emailCityDAO.removeEmailCity(email, city);
        }
        emailDAO.delete(request.email());
        logger.info("Deleting user with email {} ended successfully", request.email());
    }

    @Override
    public void addCity(EmailCitiesRequest request) {
        logger.info("Start adding cities {} to user with email {}", request.cities(), request.email());
        Email email = emailDAO.findByID(request.email());
        if (email == null) {
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            emailCityDAO.addEmailCity(email, city);
        }
        logger.info("Adding cities {} to user with email {} ended successfully", request.cities(), request.email());
    }

    @Override
    public List<CityPK> getCities(EmailRequest request) {
        logger.info("Start getting all cities for user with email {}", request.email());
        Email email = emailDAO.findByID(request.email());
        if (email == null) {
            logger.warn("Getting all cities for user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }

        Set<City> cities = email.getCities();
        logger.warn("Getting all cities for user with email {} ended successfully", request.email());
        return cities.stream().map(City::getPk).toList();
    }

    @Override
    public void deleteCities(EmailCitiesRequest request) {
        logger.info("Start removing cities {} from user with email {}", request.cities(), request.email());
        Email email = emailDAO.findByID(request.email());
        if (email == null) {
            logger.warn("Removing user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        for (CityDTO cityDTO : request.cities()) {
            City city = cityDAO.findByID(new CityPK(cityDTO.city(), cityDTO.country()));
            if (city == null) {
                logger.error("Removing went wrong because {} not in database", cityDTO);
                throw new EntityNotFoundException("City not found");
            }
            emailCityDAO.removeEmailCity(email, city);
        }
        logger.info("Removing cities {} from user with email {} ended successfully", request.cities(), request.email());
    }
}
