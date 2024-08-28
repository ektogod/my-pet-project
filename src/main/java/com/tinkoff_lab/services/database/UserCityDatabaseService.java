package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.UserCityDAO;
import com.tinkoff_lab.entity.UserCity;
import com.tinkoff_lab.entity.UserCityKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserCityDatabaseService {
    private final UserCityDAO dao;

    @Autowired
    public UserCityDatabaseService(UserCityDAO dao) {
        this.dao = dao;
    }

    public UserCityKey insert(UserCity entity) {
        return dao.insert(entity);
    }

    public UserCity findByID(UserCityKey id) {
        return dao.findByID(id);
    }

    public List<UserCity> findAll() {
        return dao.findAll();
    }

    public void update(UserCity entity) {
        dao.update(entity);
    }

    public void delete(UserCityKey id) {
        dao.delete(id);
    }
}
