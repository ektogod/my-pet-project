package com.tinkoff_lab.services.weather;

import com.tinkoff_lab.dto.CityDTO;
import com.tinkoff_lab.dto.weather.request.DeleteUserRequest;
import com.tinkoff_lab.dto.weather.request.WeatherRequest;
import com.tinkoff_lab.entity.*;
import com.tinkoff_lab.services.database.CityDatabaseService;
import com.tinkoff_lab.services.database.UserCityDatabaseService;
import com.tinkoff_lab.services.database.UserDatabaseService;
import com.tinkoff_lab.services.weather.WeatherService;
import com.tinkoff_lab.validation.CityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final UserDatabaseService userDatabaseService;
    private final CityDatabaseService cityDatabaseService;
    private final UserCityDatabaseService userCityDatabaseService;
    private final CityValidator cityValidator;

    @Autowired
    public WeatherServiceImpl(UserDatabaseService userDatabaseService, CityDatabaseService cityDatabaseService, UserCityDatabaseService userCityDatabaseService, CityValidator cityValidator) {
        this.userDatabaseService = userDatabaseService;
        this.cityDatabaseService = cityDatabaseService;
        this.userCityDatabaseService = userCityDatabaseService;
        this.cityValidator = cityValidator;
    }

    @Override
    public void add(WeatherRequest weatherResponse) {
        User user = new User(weatherResponse.email(), weatherResponse.name());
        if (userDatabaseService.findByID(user.getEmail()).equals(user)){

        }

        userDatabaseService.insert(user);
        for(CityDTO cityDTO: weatherResponse.cities()){
            cityValidator.validate(cityDTO); // throws exception if something incorrect

            CityPK pk = new CityPK(cityDTO.city(), cityDTO.country());
            City city = new City(pk, "", "");
            cityDatabaseService.insert(city);
            userCityDatabaseService.addUserCity(user, city);
        }
    }

    @Override
    public void deleteUser(DeleteUserRequest request) {
        userDatabaseService.delete(request.email());
    }
}
