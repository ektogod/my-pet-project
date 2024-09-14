package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dao.hibernate.CityDAO;
import com.tinkoff_lab.dao.hibernate.UserCityDAO;
import com.tinkoff_lab.dao.hibernate.UserDAO;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.dto.weather.CityDTO;
import com.tinkoff_lab.dto.weather.Coordinates;
import com.tinkoff_lab.dto.weather.request.email.EmailCitiesRequest;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.User;
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
    UserDAO userDAO;
    CityDAO cityDAO;
    UserCityDAO userCityDAO;
    Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    CoordinatesDefiner definer;

    @Override
    public void add(WeatherEmailRequest request) {
        logger.info("Start adding user with email {} and cities {} to database", request.email(), request.cities());
        User user = new User(request.email(), request.name());
        if (userDAO.findByID(user.getEmail()) != null) {
            logger.warn("User with email {} already exists!", user.getEmail());
            throw new WrongWeatherRequestException(String.format("User with email %s already exists!", user));
        }

        userDAO.insert(user);
        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            userCityDAO.addUserCity(user, city);
            //user.getCities().add(city);
        }

        logger.info("Adding user with email {} and cities {} to database ended successfully", user.getEmail(), user.getCities());
    }

    @Override
    public void deleteUser(EmailRequest request) {
        logger.info("Start deleting user with email {}", request);
        User user = userDAO.findByID(request.email());
        if (user == null) {
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        Set<City> cities = new HashSet<>(user.getCities());
        for (City city : cities) {
            userCityDAO.removeUserCity(user, city);
        }
        userDAO.delete(request.email());
        logger.info("Deleting user with email {} ended successfully", request.email());
    }

    @Override
    public void addCity(EmailCitiesRequest request) {
        logger.info("Start adding cities {} to user with email {}", request.cities(), request.email());
        User user = userDAO.findByID(request.email());
        if (user == null) {
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            userCityDAO.addUserCity(user, city);
        }
        logger.info("Adding cities {} to user with email {} ended successfully", request.cities(), request.email());
    }

    @Override
    public List<CityPK> getCities(EmailRequest request) {
        logger.info("Start getting all cities for user with email {}", request.email());
        User user = userDAO.findByID(request.email());
        if (user == null) {
            logger.warn("Getting all cities for user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }

        Set<City> cities = user.getCities();
        logger.warn("Getting all cities for user with email {} ended successfully", request.email());
        return cities.stream().map(City::getPk).toList();
    }

    @Override
    public void deleteCities(EmailCitiesRequest request) {
        logger.info("Start removing cities {} from user with email {}", request.cities(), request.email());
        User user = userDAO.findByID(request.email());
        if (user == null) {
            logger.warn("Removing user with email {} went wrong: user not found", request.email());
            throw new EntityNotFoundException("User not found");
        }
        for (CityDTO cityDTO : request.cities()) {
            City city = cityDAO.findByID(new CityPK(cityDTO.city(), cityDTO.country()));
            if (city == null) {
                logger.error("Removing went wrong because {} not in database", cityDTO);
                throw new EntityNotFoundException("City not found");
            }
            userCityDAO.removeUserCity(user, city);
        }
        logger.info("Removing cities {} from user with email {} ended successfully", request.cities(), request.email());
    }
}
