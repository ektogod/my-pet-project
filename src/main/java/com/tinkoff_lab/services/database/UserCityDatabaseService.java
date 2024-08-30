package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.UserCityDAO;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCityDatabaseService {
    private final UserCityDAO dao;

    @Autowired
    public UserCityDatabaseService(UserCityDAO dao) {
        this.dao = dao;
    }

    public void addUserCity(User user, City city) {
        dao.addUserCity(user, city);
    }

    public void removeUserCity(User user, City city) {
        dao.removeUserCity(user, city);
    }

    public List<User> getUsers(City city) {
        return dao.getUsers(city);
    }

    public List<City> getCities(User user) {
        return dao.getCities(user);
    }
}
