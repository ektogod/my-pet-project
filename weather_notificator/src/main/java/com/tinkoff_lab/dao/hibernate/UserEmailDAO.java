package com.tinkoff_lab.dao.hibernate;

import com.tinkoff_lab.entity.Email;
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

public class UserEmailDAO {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(UserEmailDAO.class);
    public void addUserEmail(User user, Email email) {
        logger.info("Start adding UserEmail where User: {}, Email: {}", user, email);
        Session session = sessionFactory.getCurrentSession();
        try {
            User existingUser = session.get(User.class, user.getChatId());
            Email existingEmail = session.get(Email.class, email.getEmail());

            if (existingUser == null) {
                logger.error("Adding went wrong because {} not in database", user);
                throw new EntityNotFoundException("User not found");
            }
            if (existingEmail == null) {
                logger.error("Adding went wrong because {} not in database", email);
                throw new EntityNotFoundException("Email not found");
            }

            Hibernate.initialize(user.getCities());
            Hibernate.initialize(email.getUsers());

            existingUser.getEmails().add(email);
            existingEmail.getUsers().add(user);
            session.merge(existingUser);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Adding UserEmail where User: {}, Email: {} went wrong", user, email);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Adding UserEmail where User: {}, Email: {} ended successfully", user, email);
    }

    public void removeUserEmail(User user, Email email) {
        logger.info("Start removing UserEmail where User: {}, Email: {}", user, email);
        try {
            Session session = sessionFactory.getCurrentSession();
            User existingUser = session.get(User.class, user.getChatId());
            Email existingEmail = session.get(Email.class, email.getEmail());

            if (existingUser == null) {
                logger.error("Removing went wrong because {} not in database", user);
                throw new EntityNotFoundException("User not found");
            }
            if (existingEmail == null) {
                logger.error("Removing went wrong because {} not in database", email);
                throw new EntityNotFoundException("Email not found");
            }

            Hibernate.initialize(user.getCities());
            Hibernate.initialize(email.getUsers());

            existingUser.getEmails().remove(email);
            existingEmail.getUsers().remove(user);
            session.merge(user);
        } catch (EntityNotFoundException ex) {
            logger.error("Entity not found: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.info("Removing UserEmail where User: {}, Email: {} went wrong", user, email);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing UserEmail where User: {}, Email: {} ended successfully", user, email);
    }
}
