package database;

import com.email_sender.EmailSenderApplication;
import com.tinkoff_lab.entity.Email;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Comparator;
import java.util.List;

@Testcontainers
@SpringBootTest(classes = EmailSenderApplication.class)
@TestPropertySource(locations = "classpath:hibernate.properties")
@Transactional

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
        Email email = new Email("ektogod@mail.ru", "ektogod");
        databaseService.insert(email);

        Session session = sessionFactory.getCurrentSession();
        session.flush(); // save changes

        String createSql = "SELECT * FROM user";
        List<Email> emails = null;

        emails = session.createNativeQuery(createSql, Email.class).list(); // I use this query because I dont want to use another function from service in this method. I decided firstly ensure that at least one method works correct

        Assertions.assertNotNull(emails);
        Assertions.assertEquals(emails.size(), 1);
        Assertions.assertEquals(emails.get(0), email);

        Assertions.assertThrows(DatabaseException.class, // trying to add duplicate
                () -> databaseService.insert(new Email("ektogod@mail.ru", "ektogod")));
    }

    @Test
    public void testWithFindByIdChecking() {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        databaseService.insert(email);
        Assertions.assertEquals(email, databaseService.findByID(email.getEmail()));

        Assertions.assertNull(databaseService.findByID("kilogod@mail.ru")); // trying to find non-existent note
    }

    @Test
    public void testWithFindAllChecking() {
        List<Email> emails = List.of(
                new Email("ektobot@mail.ru", "ektogod"),
                new Email("ektodog@mail.ru", "ektogod"),
                new Email("ektogod@mail.ru", "ektogod"),
                new Email("etokto@mail.ru", "ektogod"));
        emails.forEach(databaseService::insert);
        sessionFactory.getCurrentSession().flush();

        List<Email> dbEmails = databaseService.findAll(); // order in returned list was differnet with "users" order, so I decided to add users by alphabet order and then sort them
        Assertions.assertEquals(emails.size(), dbEmails.size());
        dbEmails.sort(Comparator.comparing(Email::getEmail));

        for (int i = 0; i < emails.size(); i++) {
            Assertions.assertEquals(emails.get(i), dbEmails.get(i));
        }
    }

    @Test
    public void testWithUpdateChecking() {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        databaseService.update(email); // use update instead of insert to check what happens if we try to update non-existing note

        Email updatedEmail = new Email("ektogod@mail.ru", "kilogod");
        databaseService.update(updatedEmail);
        Assertions.assertEquals(updatedEmail, databaseService.findByID(email.getEmail()));
    }

    @Test
    public void testWithDeleteChecking() {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        databaseService.insert(email);
        sessionFactory.getCurrentSession().flush();

        List<Email> dbEmails = databaseService.findAll();
        Assertions.assertEquals(dbEmails.size(), 1);

        databaseService.delete(email.getEmail());
        sessionFactory.getCurrentSession().flush();

        dbEmails = databaseService.findAll();
        Assertions.assertEquals(dbEmails.size(), 0);

        Assertions.assertThrows(DatabaseException.class,  // trying to delete non-existent note
                () -> databaseService.delete("kilogod@mail.ru"));
    }
}

