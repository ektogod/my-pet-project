package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.exceptions.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityDAO implements DAO<City, CityPK>{
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(CityDAO.class);

    @Autowired
    public CityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CityPK insert(City entity) {
        logger.info("Start inserting entity: {}", entity);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
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
        try(Session session = sessionFactory.openSession()){
            city = session.get(City.class, id);
        }
        catch (Exception ex){
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
        try(Session session = sessionFactory.openSession()){
            cities = session.createQuery("SELECT * FROM city", City.class).list();
        }
        catch (Exception ex){
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return cities;
    }

    @Override
    public void update(City entity) {
        logger.info("Start updating entity: {}", entity);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Updating of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Updating entity {} ended successfully", entity);
    }

    @Override
    public void delete(CityPK id) {
        logger.info("Start removing entity with id {}", id);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            City city = session.get(City.class, id);
            session.remove(city);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            logger.error("Removing entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing entity with id {} ended successfully", id);
    }
}
