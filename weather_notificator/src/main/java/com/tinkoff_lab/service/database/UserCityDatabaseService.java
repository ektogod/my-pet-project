package com.tinkoff_lab.service.database;

import com.tinkoff_lab.dao.hibernate.EmailCityDAO;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserCityDatabaseService {
    private final EmailCityDAO dao;

    @Autowired
    public UserCityDatabaseService(EmailCityDAO dao) {
        this.dao = dao;
    }

    public void addUserCity(Email email, City city) {
        dao.addEmailCity(email, city);
    }

    public void removeUserCity(Email email, City city) {
        dao.removeEmailCity(email, city);
    }

    public Set<Email> getUsers(City city) {
        return dao.getUsers(city);
    }

    public Set<City> getCities(Email email) {
        return dao.getCities(email);
    }
}
