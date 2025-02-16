package com.tinkoff_lab.dao.hibernate;

import com.tinkoff_lab.dao.DAO;
import com.tinkoff_lab.entity.Email;
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

public class EmailDAO implements DAO<Email, String> {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(EmailDAO.class);

    @Override
    public String insert(Email entity) {
        logger.info("Start inserting entity: {}", entity);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);
        } catch (Exception ex) {
            logger.error("Inserting of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Inserting of entity {} ended successfully", entity);
        return entity.getEmail();
    }

    @Override
    public Email findByID(String id) {
        logger.info("Start finding entity with id {}", id);
        Email email;
        try  {
            Session session = sessionFactory.getCurrentSession();
            email = session.get(Email.class, id);
            if (email != null) {
                Hibernate.initialize(email.getCities());
            }
        } catch (Exception ex) {
            logger.error("Finding of entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding of entity with id {} ended successfully", id);
        return email;
    }

    @Override
    public List<Email> findAll() {
        logger.info("Start finding all entities");
        List<Email> emails;
        try {
            Session session = sessionFactory.getCurrentSession();
            emails = session.createNativeQuery("SELECT * FROM user", Email.class).list();
            emails.forEach(user -> {
                Hibernate.initialize(user.getCities());
            });
        } catch (Exception ex) {
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return emails;
    }

    @Override
    public void update(Email entity) {
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
    public void delete(String id) {
        logger.info("Start removing entity with id {}", id);
        try {
            Session session = sessionFactory.getCurrentSession();
            Email email = session.get(Email.class, id);
            session.remove(email);
        } catch (Exception ex) {
            logger.error("Removing entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing entity with id {} ended successfully", id);
    }
}

