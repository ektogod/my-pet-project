package com.tinkoff_lab.service.weather;

import com.tinkoff_lab.dto.CityDTO;
import com.tinkoff_lab.dto.Coordinates;
import com.tinkoff_lab.dto.weather.request.AddCityRequest;
import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.entity.*;
import com.tinkoff_lab.exception.WrongWeatherRequestException;
import com.tinkoff_lab.service.CurrentWeatherService;
import com.tinkoff_lab.service.database.CityDatabaseService;
import com.tinkoff_lab.service.database.UserCityDatabaseService;
import com.tinkoff_lab.service.database.UserDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final UserDatabaseService userDatabaseService;
    private final CityDatabaseService cityDatabaseService;
    private final UserCityDatabaseService userCityDatabaseService;
    private final CoordinateService coordinateService;
    private final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    public WeatherServiceImpl(UserDatabaseService userDatabaseService, CityDatabaseService cityDatabaseService, UserCityDatabaseService userCityDatabaseService, CoordinateService coordinateService) {
        this.userDatabaseService = userDatabaseService;
        this.cityDatabaseService = cityDatabaseService;
        this.userCityDatabaseService = userCityDatabaseService;
        this.coordinateService = coordinateService;
    }

    @Override
    public void add(WeatherRequest request) {
        logger.info("Start adding user with email {} and cities {} to database", request.email(), request.cities());
        User user = new User(request.email(), request.name());
        if (userDatabaseService.findByID(user.getEmail()) != null){
            logger.warn("User with email {} already exists!", user.getEmail());
            throw new WrongWeatherRequestException(String.format("User with email %s already exists!", user));
        }

        userDatabaseService.insert(user);
        for(CityDTO cityDTO: request.cities()){
            Coordinates crd = coordinateService.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDatabaseService.update(city);
            userCityDatabaseService.addUserCity(user, city);
            //user.getCities().add(city);
        }
        logger.info("Adding user with email {} and cities {} to database ended successfully", user.getEmail(), user.getCities());
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        logger.info("Start deleting user with email {}", request.email());
        User user = userDatabaseService.findByID(request.email());
        if(user == null){
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new WrongWeatherRequestException("User not found");
        }
        Set<City> cities = user.getCities();
        for(City city: cities){
            userCityDatabaseService.removeUserCity(user, city);
        }
        userDatabaseService.delete(request.email());
        logger.info("Deleting user with email {} ended successfully", request.email());
    }

    @Override
    public void addCity(AddCityRequest request) {
        logger.info("Start adding cities {} to user with email {}", request.cities(), request.email());
        User user = userDatabaseService.findByID(request.email());
        if(user == null){
            logger.warn("Deleting user with email {} went wrong: user not found", request.email());
            throw new WrongWeatherRequestException("User not found");
        }
        for(CityDTO cityDTO: request.cities()){
            Coordinates crd = coordinateService.getCoordinates(cityDTO.city(), cityDTO.country()); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, crd.latitude(), crd.longitude());
            cityDatabaseService.update(city);
            userCityDatabaseService.addUserCity(user, city);
        }
        logger.info("Adding cities {} to user with email {} ended successfully", request.cities(), request.email());
    }
}
