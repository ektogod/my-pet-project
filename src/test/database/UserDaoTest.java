package database;

import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.service.database.UserDatabaseService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;
import java.util.List;

@Testcontainers
@SpringBootTest(classes = TinkoffLabApplication.class)
@TestPropertySource(locations = "classpath:hibernate.properties")
public class UserDaoTest {
    private final UserDatabaseService databaseService;
    private final SessionFactory sessionFactory;

    @Container
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("tinkoff_lab_db")
            .withUsername("root")
            .withPassword("sqlkilogodH19");

    @Autowired
    public UserDaoTest(UserDatabaseService databaseService, SessionFactory sessionFactory) {
        this.databaseService = databaseService;
        this.sessionFactory = sessionFactory;
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("hibernate.connection.url", container::getJdbcUrl);
        registry.add("hibernate.connection.username", container::getUsername);
        registry.add("hibernate.connection.password", container::getPassword);
    }

    @BeforeEach
    public void init() {
        String createSql = "CREATE TABLE IF NOT EXISTS user" +
                "(email VARCHAR(255) PRIMARY KEY NOT NULL," +
                " name VARCHAR(1000) NOT NULL)";
        String deleteSql = "DELETE FROM user";
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            session.createNativeQuery(createSql, Void.class).executeUpdate();
            session.createNativeQuery(deleteSql, Void.class).executeUpdate();
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Test
    public void testWithInsertChecking() {
        User user = new User("ektogod@mail.ru", "ektogod");
        databaseService.insert(user);

        String createSql = "SELECT * FROM user";
        List<User> users = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            users = session.createNativeQuery(createSql, User.class).list(); // I use this query because I dont want to use another function from service in this method. I decided firstly ensure that at least one method works correct
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0), user);

        Assertions.assertThrows(DatabaseException.class, // trying to add duplicate
                () -> databaseService.insert(new User("ektogod@mail.ru", "ektogod")));
    }

    @Test
    public void testWithFindByIdChecking() {
        User user = new User("ektogod@mail.ru", "ektogod");
        databaseService.insert(user);
        Assertions.assertEquals(user, databaseService.findByID(user.getEmail()));

        Assertions.assertNull(databaseService.findByID("kilogod@mail.ru")); // trying to find non-existent note
    }

    @Test
    public void testWithFindAllChecking() {
        List<User> users = List.of(
                new User("ektobot@mail.ru", "ektogod"),
                new User("ektodog@mail.ru", "ektogod"),
                new User("ektogod@mail.ru", "ektogod"),
                new User("etokto@mail.ru", "ektogod"));
        users.forEach(databaseService::insert);

        List<User> dbUsers = databaseService.findAll(); // order in returned list was differnet with "users" order, so I decided to add users by alphabet order and then sort them
        Assertions.assertEquals(users.size(), dbUsers.size());
        dbUsers.sort(Comparator.comparing(User::getEmail));

        for (int i = 0; i < users.size(); i++) {
            Assertions.assertEquals(users.get(i), dbUsers.get(i));
        }
    }

    @Test
    public void testWithUpdateChecking() {
        User user = new User("ektogod@mail.ru", "ektogod");
        databaseService.update(user); // use update instead of insert to check what happens if we try to update non-existing note

        User updatedUser = new User("ektogod@mail.ru", "kilogod");
        databaseService.update(updatedUser);
        Assertions.assertEquals(updatedUser, databaseService.findByID(user.getEmail()));
    }

    @Test
    public void testWithDeleteChecking() {
        User user = new User("ektogod@mail.ru", "ektogod");
        databaseService.insert(user);

        List<User> dbUsers = databaseService.findAll();
        Assertions.assertEquals(dbUsers.size(), 1);

        databaseService.delete(user.getEmail());
        dbUsers = databaseService.findAll();
        Assertions.assertEquals(dbUsers.size(), 0);

        Assertions.assertThrows(DatabaseException.class,  // trying to delete non-existent note
                () -> databaseService.delete("kilogod@mail.ru"));
    }
}

