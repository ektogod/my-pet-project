package com.tinkoff_lab.dao.hibernate;

import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.entity.City;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class EmailCityDAO {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(UserCityDAO.class);

    public void addEmailCity(Email email, City city) {
        logger.info("Start adding UserCity where User: {}, City: {}", email, city);
        Session session = sessionFactory.getCurrentSession();
        try {
            Email existingEmail = session.get(Email.class, email.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingEmail == null) {
                logger.error("Adding went wrong because {} not in database", email);
                throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Adding went wrong because {} not in database", city);
                throw new EntityNotFoundException("City not found");
            }

            Hibernate.initialize(email.getCities());
            Hibernate.initialize(city.getEmails());

            existingEmail.getCities().add(city);
            existingCity.getEmails().add(email);
            session.merge(existingEmail);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Adding UserCity where User: {}, City: {} went wrong", email, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Adding UserCity where User: {}, City: {} ended successfully", email, city);
    }

    public void removeEmailCity(Email email, City city) {
        logger.info("Start removing UserCity where User: {}, City: {}", email, city);
        try {
            Session session = sessionFactory.getCurrentSession();
            Email existingEmail = session.get(Email.class, email.getEmail());
            City existingCity = session.get(City.class, city.getPk());

            if (existingEmail == null) {
                logger.error("Removing went wrong because {} not in database", email);
                throw new EntityNotFoundException("User not found");
            }
            if (existingCity == null) {
                logger.error("Removing went wrong because {} not in database", city);
                throw new EntityNotFoundException("City not found");
            }

            Hibernate.initialize(email.getCities());
            Hibernate.initialize(city.getEmails());

            existingEmail.getCities().remove(city);
            existingCity.getEmails().remove(email);
            session.merge(email);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Removing UserCity where User: {}, City: {} went wrong", email, city);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing UserCity where User: {}, City: {} ended successfully", email, city);
    }

    public Set<Email> getUsers(City city) {
        return city.getEmails();
    }

    public Set<City> getCities(Email email) {
        return email.getCities();
    }
}
