package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.UserCity;
import com.tinkoff_lab.entity.UserCityKey;
import com.tinkoff_lab.exceptions.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserCityDAO implements DAO<UserCity, UserCityKey>{
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(UserCityDAO.class);

    @Autowired
    public UserCityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public UserCityKey insert(UserCity entity) {
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
        return entity.getId();
    }

    @Override
    public UserCity findByID(UserCityKey id) {
        logger.info("Start finding entity with id {}", id);
        UserCity userCity;
        try(Session session = sessionFactory.openSession()){
            userCity = session.get(UserCity.class, id);
        }
        catch (Exception ex){
            logger.error("Finding of entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding of entity with id {} ended successfully", id);
        return userCity;
    }

    @Override
    public List<UserCity> findAll() {
        logger.info("Start finding all entities");
        List<UserCity> userCities;
        try(Session session = sessionFactory.openSession()){
            userCities = session.createQuery("SELECT * FROM user-city", UserCity.class).list();
        }
        catch (Exception ex){
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return userCities;
    }

    @Override
    public void update(UserCity entity) {
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
    public void delete(UserCityKey id) {
        logger.info("Start removing entity with id {}", id);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            UserCity userCity = session.get(UserCity.class, id);
            session.remove(userCity);
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
