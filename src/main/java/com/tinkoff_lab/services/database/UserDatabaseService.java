package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.UserDAO;
import com.tinkoff_lab.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDatabaseService {
    private final UserDAO dao;

    @Autowired
    public UserDatabaseService(UserDAO dao) {
        this.dao = dao;
    }

    public int insert(User entity) {
        return dao.insert(entity);
    }

    public User findByID(int id) {
        return dao.findByID(id);
    }

    public List<User> findAll() {
        return dao.findAll();
    }

    public void update(User entity) {
        dao.update(entity);
    }

    public void delete(int id) {
        dao.delete(id);
    }
}
