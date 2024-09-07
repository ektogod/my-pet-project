package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.exception.DatabaseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class CityDAO implements DAO<City, CityPK> {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(CityDAO.class);

    @Override
    public CityPK insert(City entity) {
        logger.info("Start inserting entity: {}", entity);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);
        } catch (Exception ex) {
            logger.error("Inserting of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Inserting of entity {} ended successfully", entity);
        return entity.getPk();
    }

    @Override
    public City findByID(CityPK id) {
        logger.info("Start finding entity with id {}", id);
        City city;
        try {
            Session session = sessionFactory.getCurrentSession();
            city = session.get(City.class, id);
            if (city != null) {
                Hibernate.initialize(city.getUsers());
            }
        } catch (Exception ex) {
            logger.error("Finding of entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding of entity with id {} ended successfully", id);
        return city;
    }

    @Override
    public List<City> findAll() {
        logger.info("Start finding all entities");
        List<City> cities;
        try {
            Session session = sessionFactory.getCurrentSession();
            cities = session.createNativeQuery("SELECT * FROM city", City.class).list();
            cities.forEach(city -> {
                Hibernate.initialize(city.getUsers());
            });
        } catch (Exception ex) {
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return cities;
    }

    @Override
    public void update(City entity) {
        logger.info("Start updating entity: {}", entity);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.merge(entity);
        } catch (Exception ex) {
            logger.error("Updating of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Updating entity {} ended successfully", entity);
    }

    @Override
    public void delete(CityPK id) {
        logger.info("Start removing entity with id {}", id);
        try {
            Session session = sessionFactory.getCurrentSession();
            City city = session.get(City.class, id);
            session.remove(city);
        } catch (Exception ex) {
            logger.error("Removing entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Removing entity with id {} ended successfully", id);
    }
}
