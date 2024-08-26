package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.dto.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationDatabaseService {
    private TranslationDAO dao;

    @Autowired
    public TranslationDatabaseService(TranslationDAO dao) {
        this.dao = dao;
    }

    public int insert(Translation entity){
        return dao.insert(entity);
    }

    public Translation findByID(int id){
        return dao.findByID(id);
    }

    public List<Translation> findAll(){
        return dao.findAll();
    }

    public void update(int id, Translation entity){
        dao.update(id, entity);
    }

    public void delete(int id){
        dao.delete(id);
    }
}
