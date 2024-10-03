package com.tinkoff_lab.dao.hibernate;

import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.exception.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class UserCityDAO {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(EmailCityDAO.class);
    public void addTelegramUserCity(User user, City city) {
        logger.info("Start adding TelegramUserCity where TelegramUser: {}, City: {}", user, city);
        Session session = sessionFactory.getCurrentSession();
        try {
            User existingUser = session.get(User.class, user.getChatId());
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
            Hibernate.initialize(city.getTgUsers());

            existingUser.getCities().add(city);
            existingCity.getTgUsers().add(user);
            session.merge(existingUser);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Adding TelegramUserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Adding TelegramUserCity where User: {}, City: {} ended successfully", user, city);
    }

    public void removeUserCity(User user, City city) {
        logger.info("Start removing TelegramUserCity where User: {}, City: {}", user, city);
        try {
            Session session = sessionFactory.getCurrentSession();
            User existingUser = session.get(User.class, user.getChatId());
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
            Hibernate.initialize(city.getTgUsers());

            existingUser.getCities().remove(city);
            existingCity.getTgUsers().remove(user);
            session.merge(user);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Removing TelegramUserCity where User: {}, City: {} went wrong", user, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing TelegramUserCity where User: {}, City: {} ended successfully", user, city);
    }
}
