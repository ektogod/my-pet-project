package com.tinkoff_lab.service.database;

import com.tinkoff_lab.dao.UserCityDAO;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    public Set<User> getUsers(City city) {
        return dao.getUsers(city);
    }

    public Set<City> getCities(User user) {
        return dao.getCities(user);
    }
}
