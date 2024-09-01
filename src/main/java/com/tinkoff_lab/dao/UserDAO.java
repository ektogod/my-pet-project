package com.tinkoff_lab.dao;

import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.exception.DatabaseException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO implements DAO<User, String>{
    private final SessionFactory sessionFactory;
    private final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String insert(User entity) {
        logger.info("Start inserting entity: {}", entity);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            session.persist(entity);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK){
                transaction.rollback();
            }
            logger.error("Inserting of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }
        finally {
            session.close();
        }

        logger.info("Inserting of entity {} ended successfully", entity);
        return entity.getEmail();
    }

    @Override
    public User findByID(String id) {
        logger.info("Start finding entity with id {}", id);
        User user;
        try(Session session = sessionFactory.openSession()){
            user = session.get(User.class, id);
            if(user != null){
                Hibernate.initialize(user.getCities());
            }
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
            users = session.createNativeQuery("SELECT * FROM user", User.class).list();
            users.forEach(user -> {Hibernate.initialize(user.getCities());});
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            session.merge(entity);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK){
                transaction.rollback();
            }
            logger.error("Updating of entity {} went wrong", entity);
            throw new DatabaseException(ex.getMessage());
        }
        finally {
            session.close();
        }
        logger.info("Updating entity {} ended successfully", entity);
    }

    @Override
    public void delete(String id) {
        logger.info("Start removing entity with id {}", id);
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try{
            transaction.begin();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
        }
        catch (Exception ex){
            if(transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK){
                transaction.rollback();
            }
            logger.error("Removing entity with id {} went wrong", id);
            throw new DatabaseException(ex.getMessage());
        }
        finally {
            session.close();
        }
        logger.info("Removing entity with id {} ended successfully", id);
    }
}

