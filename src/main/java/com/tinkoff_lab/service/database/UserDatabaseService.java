package com.tinkoff_lab.service.database;

import com.tinkoff_lab.dao.hibernate.UserDAO;
import com.tinkoff_lab.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDatabaseService {
    private final UserDAO dao;

    @Autowired
    public UserDatabaseService(UserDAO dao) {
        this.dao = dao;
    }

    public String insert(User entity) {
        return dao.insert(entity);
    }

    public User findByID(String id) {
        return dao.findByID(id);
    }

    public List<User> findAll() {
        return dao.findAll();
    }

    public void update(User entity) {
        dao.update(entity);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
