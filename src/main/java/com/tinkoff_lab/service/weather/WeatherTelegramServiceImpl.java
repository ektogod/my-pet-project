package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dao.hibernate.CityDAO;
import com.tinkoff_lab.dao.hibernate.TelegramUserCityDAO;
import com.tinkoff_lab.dao.hibernate.TelegramUserDAO;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramCitiesRequest;
import com.tinkoff_lab.dto.weather.request.telegram.TelegramRequest;
import com.tinkoff_lab.dto.weather.request.telegram.WeatherTelegramRequest;
import com.tinkoff_lab.entity.TelegramUser;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.dto.weather.CityDTO;
import com.tinkoff_lab.dto.weather.Coordinates;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.external.CoordinatesDefiner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional(transactionManager = "transactionManager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class WeatherTelegramServiceImpl implements WeatherTelegramService {
    TelegramUserDAO tgUserRep;
    CityDAO cityDAO;
    TelegramUserCityDAO tgUserCityDAO;
    Logger logger = LoggerFactory.getLogger(WeatherTelegramServiceImpl.class);
    CoordinatesDefiner definer;

    @Override
    public void add(WeatherTelegramRequest request) {
        logger.info("Start adding user with username {} and cities {} to database", request.username(), request.cities());
        TelegramUser user = tgUserRep.findByID(request.chatId());
        if (tgUserRep.findByID(user.getChatId()) == null) {
            user = new TelegramUser(
                    request.chatId(),
                    request.username(),
                    request.firstname(),
                    request.lastname());
            tgUserRep.insert(user);
        }

        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            tgUserCityDAO.addTelegramUserCity(user, city);
            //user.getCities().add(city);
        }

        logger.info("Adding user with username {} and cities {} to database ended successfully", user.getUsername(), user.getCities());
    }

    @Override
    public void deleteUser(TelegramRequest request) {
        logger.info("Start deleting user with username {}", request.username());
        TelegramUser user = tgUserRep.findByID(request.chatId());
        if (user == null) {
            logger.warn("Deleting user with username {} went wrong: user not found", request.username());
            throw new EntityNotFoundException("User not found");
        }

        Set<City> cities = new HashSet<>(user.getCities());
        for (City city : cities) {
            tgUserCityDAO.removeUserCity(user, city);
        }
        tgUserRep.delete(user.getChatId());
        logger.info("Deleting user with username {} ended successfully", request.username());
    }

    @Override
    public void addCity(TelegramCitiesRequest request) {
        logger.info("Start adding cities {} to user with username {}", request.cities(), request.username());
        TelegramUser user = tgUserRep.findByID(request.chatId());
        if (user == null) {
            logger.warn("Deleting user with username {} went wrong: user not found", request.username());
            throw new EntityNotFoundException("User not found");
        }

        for (CityDTO cityDTO : request.cities()) {
            Coordinates crd = definer.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDAO.update(city);
            tgUserCityDAO.addTelegramUserCity(user, city);
        }
        logger.info("Adding cities {} to user with email {} ended successfully", request.cities(), request.username());
    }

    @Override
    public List<CityPK> getCities(TelegramRequest request) {
        logger.info("Start getting all cities for user with username {}", request.username());
        TelegramUser user = tgUserRep.findByID(request.chatId());
        if (user == null) {
            logger.warn("Getting all cities for user with username {} went wrong: user not found", request.username());
            throw new EntityNotFoundException("User not found");
        }

        Set<City> cities = user.getCities();
        logger.warn("Getting all cities for user with username {} ended successfully", request.username());
        return cities.stream().map(City::getPk).toList();
    }

    @Override
    public void deleteCities(TelegramCitiesRequest request) {
        logger.info("Start removing cities {} from user with username {}", request.cities(), request.username());
        TelegramUser user = tgUserRep.findByID(request.chatId());
        if (user == null) {
            logger.warn("Removing user with username {} went wrong: user not found", request.username());
            throw new EntityNotFoundException("User not found");
        }

        for (CityDTO cityDTO : request.cities()) {
            City city = cityDAO.findByID(new CityPK(cityDTO.city(), cityDTO.country()));
            if (city == null) {
                logger.error("Removing went wrong because {} not in database", cityDTO);
                throw new EntityNotFoundException("City not found");
            }
            tgUserCityDAO.removeUserCity(user, city);
        }
        logger.info("Removing cities {} from user with username {} ended successfully", request.cities(), request.username());
    }
}
