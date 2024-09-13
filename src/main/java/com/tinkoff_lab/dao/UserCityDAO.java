package com.tinkoff_lab.dao;

import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class UserCityDAO {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(UserCityDAO.class);

    public void addUserCity(User user, City city) {
        logger.info("Start adding UserCity where User: {}, City: {}", user, city);
        Session session = sessionFactory.getCurrentSession();
        try {
            User existingUser = session.get(User.class, user.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingUser == null) {
                logger.error("Adding went wrong because {} not in database", user);
                throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Adding went wrong because {} not in database", city);
                throw new EntityNotFoundException("City not found");
            }

            Hibernate.initialize(user.getCities());
            Hibernate.initialize(city.getUsers());

            existingUser.getCities().add(city);
            existingCity.getUsers().add(user);
            session.merge(existingUser);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Adding UserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Adding UserCity where User: {}, City: {} ended successfully", user, city);
    }

    public void removeUserCity(User user, City city) {
        logger.info("Start removing UserCity where User: {}, City: {}", user, city);
        try {
            Session session = sessionFactory.getCurrentSession();
            User existingUser = session.get(User.class, user.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingUser == null) {
                logger.error("Removing went wrong because {} not in database", user);
                throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Removing went wrong because {} not in database", city);
                throw new EntityNotFoundException("City not found");
            }

            Hibernate.initialize(user.getCities());
            Hibernate.initialize(city.getUsers());

            existingUser.getCities().remove(city);
            existingCity.getUsers().remove(user);
            session.merge(user);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Removing UserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing UserCity where User: {}, City: {} ended successfully", user, city);
    }

    public Set<User> getUsers(City city) {
        return city.getUsers();
    }

    public Set<City> getCities(User user) {
        return user.getCities();
    }
}
