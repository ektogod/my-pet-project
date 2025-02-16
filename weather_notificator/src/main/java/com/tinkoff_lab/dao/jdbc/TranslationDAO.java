package com.tinkoff_lab.dao.jdbc;

import com.tinkoff_lab.dao.DAO;
import com.tinkoff_lab.dto.translation.Translation;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.service.database.ConnectionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class TranslationDAO implements DAO<Translation, Integer> {            // class for saving records in database
    ConnectionService connectionService;
    Logger logger = LoggerFactory.getLogger(TranslationDAO.class);

    @Override
    public Integer insert(Translation entity) {
        logger.info("Start writing to database of query: {}", entity);

        String sql = "INSERT INTO query (IP, Original_Text, Original_Language, Translated_Text, Target_Language, Time, Status, Message) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionService.getConnection()) {    // getting connection with db
            logger.info("Connection with database established");

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepareStatement(statement, entity);
            statement.executeUpdate();  // sending to db

            int key = getNewID(statement, entity);
            logger.info("Query has been successfully recorded");

            return key; // returns id of a new note
        } catch (SQLException ex) {
            logger.error("Insertion of query in database goes wrong!");
            throw new DatabaseException("Insertion of query in database goes wrong!");
        }
    }

    @Override
    public List<Translation> findAll() {
        logger.info("Start getting all entities from database");
        String sql = "SELECT * FROM query";

        List<Translation> translations = new ArrayList<>();
        try (Connection connection = connectionService.getConnection()) {
            logger.info("Connection with database established");

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                translations.add(new Translation(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(8),
                        resultSet.getString(9)));
            }

            logger.info("Stop getting all entities from database");
        } catch (SQLException e) {
            logger.error("Finding all entities in database goes wrong!");
            throw new DatabaseException("Finding all entities in database goes wrong!");
        }

        return translations;
    }

    @Override
    public void update(Translation entity) {
        logger.info("Starting updating entity: {}", entity);
        String sql = "UPDATE query " +
                "SET IP = ?, " +
                "Original_Text = ?, " +
                "Original_Language = ?, " +
                "Translated_Text = ?, " +
                "Target_Language = ?, " +
                "Time = ?, " +
                "Status = ?, " +
                "Message = ? " +
                "WHERE ID = ?";

        try (Connection connection = connectionService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            prepareStatement(statement, entity);
            statement.setInt(9, entity.id());

            statement.executeUpdate();
            logger.info("Updating ended successfully for entity: {}", entity);
        } catch (SQLException e) {
            logger.error("Updating of entity goes wrong! Entity: " + entity);
            throw new DatabaseException("Updating of entity goes wrong!");
        }
    }

    @Override
    public void delete(Integer id) {
        logger.info("Starting deleting entity with ID: {}", id);
        String sql = "DELETE FROM query WHERE ID = ?";

        try (Connection connection = connectionService.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Deleting entity with ID {} ended successfully.", id);
        } catch (SQLException e) {
            logger.error("Deleting entity with id " + id + "goes wrong!");
            throw new DatabaseException("Deleting entity goes wrong!");
        }
    }

    @Override       // this method I used in test classes
    public Translation findByID(Integer id) {
        String sql = "SELECT * FROM query WHERE ID = ?";
        logger.info("Starting finding entity with ID: {}", id);
        try (Connection connection = connectionService.getConnection()) {    // getting connection with db
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Translation(id,
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(8),
                        resultSet.getString(9));
            }

            logger.info("Finding entity with ID {} ended successfully.", id);
        } catch (SQLException ex) {
            logger.info("Getting query by id " + id + " goes wrong!");
            throw new DatabaseException("Getting query by id goes wrong!");
        }

        return null;
    }

    private void prepareStatement(PreparedStatement statement, Translation entity) throws SQLException {
        statement.setString(1, entity.ip());  // preparing the statement
        statement.setString(2, entity.originalText());
        statement.setString(3, entity.originalLang());
        statement.setString(4, entity.translatedText());
        statement.setString(5, entity.targetLang());
        statement.setString(6, entity.time());
        statement.setInt(7, entity.status());
        statement.setString(8, entity.message());
    }

    private int getNewID(PreparedStatement statement, Translation entity) throws SQLException {
        ResultSet generatedKeys = statement.getGeneratedKeys();
        int key = -1;
        if (generatedKeys != null && generatedKeys.next()) {
            key = generatedKeys.getInt(1); // getting ID
        } else {
            logger.error("Creating translation failed, no ID obtained. Translation: {}", entity);
            throw new DatabaseException("Creating translation failed, no ID obtained.");
        }

        return key;
    }
}
