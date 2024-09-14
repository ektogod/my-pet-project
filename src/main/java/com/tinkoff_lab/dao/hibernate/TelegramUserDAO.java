package com.tinkoff_lab.dao.hibernate;

import com.tinkoff_lab.dao.DAO;
import com.tinkoff_lab.entity.TelegramUser;
import com.tinkoff_lab.entity.User;
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

public class TelegramUserDAO implements DAO<TelegramUser, Long> {
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(TelegramUserDAO.class);

    @Override
    public Long insert(TelegramUser entity) {
        logger.info("Start inserting entity: {}", entity);
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);
        } catch (Exception ex) {
            logger.error("Inserting of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Inserting of entity {} ended successfully", entity);
        return entity.getChatId();
    }

    @Override
    public TelegramUser findByID(Long id) {
        logger.info("Start finding entity with id {}", id);
        TelegramUser user;
        try  {
            Session session = sessionFactory.getCurrentSession();
            user = session.get(TelegramUser.class, id);
            if (user != null) {
                Hibernate.initialize(user.getCities());
            }
        } catch (Exception ex) {
            logger.error("Finding of entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding of entity with id {} ended successfully", id);
        return user;
    }

    @Override
    public List<TelegramUser> findAll() {
        logger.info("Start finding all entities");
        List<TelegramUser> users;
        try {
            Session session = sessionFactory.getCurrentSession();
            users = session.createNativeQuery("SELECT * FROM tg-user", TelegramUser.class).list();
            users.forEach(user -> {
                Hibernate.initialize(user.getCities());
            });
        } catch (Exception ex) {
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return users;
    }

    @Override
    public void update(TelegramUser entity) {
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
    public void delete(Long id) {
        logger.info("Start removing entity with id {}", id);
        try {
            Session session = sessionFactory.getCurrentSession();
            TelegramUser user = session.get(TelegramUser.class, id);
            session.remove(user);
        } catch (Exception ex) {
            logger.error("Removing entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }
        logger.info("Removing entity with id {} ended successfully", id);
    }
}
