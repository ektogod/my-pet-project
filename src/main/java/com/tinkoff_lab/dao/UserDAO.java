package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.exceptions.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDAO implements DAO<User, Integer>{
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Integer insert(User entity) {
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
    public User findByID(Integer id) {
        logger.info("Start finding entity with id {}", id);
        User user;
        try(Session session = sessionFactory.openSession()){
            user = session.get(User.class, id);
        }
        catch (Exception ex){
            logger.error("Finding of entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding of entity with id {} ended successfully", id);
        return user;
    }

    @Override
    public List<User> findAll() {
        logger.info("Start finding all entities");
        List<User> users;
        try(Session session = sessionFactory.openSession()){
            users = session.createQuery("SELECT * FROM user", User.class).list();
        }
        catch (Exception ex){
            logger.error("Something went wrong with finding all entities");
            throw new DatabaseException(ex.getMessage());
        }

        logger.info("Finding ended successfully");
        return users;
    }

    @Override
    public void update(User entity) {
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
    public void delete(Integer id) {
        logger.info("Start removing entity with id {}", id);
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()){
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
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

