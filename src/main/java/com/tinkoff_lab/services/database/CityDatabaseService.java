package com.tinkoff_lab.services.database;

import com.tinkoff_lab.dao.CityDAO;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityDatabaseService {
    private final CityDAO dao;

    @Autowired
    public CityDatabaseService(CityDAO dao) {
        this.dao = dao;
    }

    public CityPK insert(City entity){
        return dao.insert(entity);
    }

    public City findByID(CityPK id){
        return dao.findByID(id);
    }

    public List<City> findAll(){
        return dao.findAll();
    }

    public void update(City entity){
        dao.update(entity);
    }

    public void delete(CityPK id){
        dao.delete(id);
    }
}
