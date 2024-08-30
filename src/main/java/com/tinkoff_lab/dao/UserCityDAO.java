package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.*;
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
public class UserCityDAO {
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(UserCityDAO.class);

    @Autowired
    public UserCityDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addUserCity(User user, City city){
        logger.info("Start adding UserCity where User: {}, City: {}", user, city);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            User existingUser = session.get(User.class, user.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingUser == null) {
                logger.error("Adding went wrong because {} not in database", user);
                //throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Adding went wrong because {} not in database", city);
                //throw new EntityNotFoundException("City not found");
            }

            transaction = session.beginTransaction();
            user.getCities().add(city);
            city.getUsers().add(user);
            session.merge(user);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            logger.info("Adding UserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Adding UserCity where User: {}, City: {} ended successfully", user, city);
    }

    public void removeUserCity(User user, City city){
        logger.info("Start removing UserCity where User: {}, City: {}", user, city);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            User existingUser = session.get(User.class, user.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingUser == null) {
                logger.error("Adding went wrong because {} not in database", user);
                //throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Adding went wrong because {} not in database", city);
                //throw new EntityNotFoundException("City not found");
            }

            transaction = session.beginTransaction();
            user.getCities().remove(city);
            city.getUsers().remove(user);
            session.merge(user);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction != null){
                transaction.rollback();
            }
            logger.info("Removing UserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing UserCity where User: {}, City: {} ended successfully", user, city);
    }

    public List<User> getUsers(City city){
        return city.getUsers();
    }

    public List<City> getCities(User user){
        return user.getCities();
    }

}
