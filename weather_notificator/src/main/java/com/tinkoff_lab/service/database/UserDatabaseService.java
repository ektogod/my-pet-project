package com.tinkoff_lab.service.database;

import com.tinkoff_lab.dao.hibernate.EmailDAO;
import com.tinkoff_lab.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDatabaseService {
    private final EmailDAO dao;

    @Autowired
    public UserDatabaseService(EmailDAO dao) {
        this.dao = dao;
    }

    public String insert(Email entity) {
        return dao.insert(entity);
    }

    public Email findByID(String id) {
        return dao.findByID(id);
    }

    public List<Email> findAll() {
        return dao.findAll();
    }

    public void update(Email entity) {
        dao.update(entity);
    }

    public void delete(String id) {
        dao.delete(id);
    }
}
