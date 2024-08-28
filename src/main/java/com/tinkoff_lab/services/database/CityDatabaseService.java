package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.CityDAO;
import com.tinkoff_lab.entity.City;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CityDatabaseService {
    private final CityDAO dao;

    @Autowired
    public CityDatabaseService(CityDAO dao) {
        this.dao = dao;
    }

    public int insert(City entity){
        return dao.insert(entity);
    }

    public City findByID(int id){
        return dao.findByID(id);
    }

    public List<City> findAll(){
        return dao.findAll();
    }

    public void update(City entity){
        dao.update(entity);
    }

    public void delete(int id){
        dao.delete(id);
    }
}
