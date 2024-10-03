package database;

import com.email_sender.EmailSenderApplication;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.service.database.CityDatabaseService;
import com.tinkoff_lab.service.database.UserCityDatabaseService;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Testcontainers
@SpringBootTest(classes = EmailSenderApplication.class)
@TestPropertySource(locations = "classpath:hibernate.properties")
@Transactional

public class UserCityDaoTest {
    private final UserCityDatabaseService userCityDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final CityDatabaseService cityDatabaseService;
    private final SessionFactory sessionFactory;

    @Container
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("tinkoff_lab_db")
            .withUsername("root")
            .withPassword("sqlkilogodH19");

    @Autowired
    public UserCityDaoTest(UserCityDatabaseService userCityDatabaseService, UserDatabaseService userDatabaseService, CityDatabaseService cityDatabaseService, SessionFactory sessionFactory) {
        this.userCityDatabaseService = userCityDatabaseService;
        this.userDatabaseService = userDatabaseService;
        this.cityDatabaseService = cityDatabaseService;
        this.sessionFactory = sessionFactory;
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("hibernate.connection.url", container::getJdbcUrl);
        registry.add("hibernate.connection.username", container::getUsername);
        registry.add("hibernate.connection.password", container::getPassword);
    }

    @BeforeEach
    public void setUp() {
        String createCitySql = "CREATE TABLE IF NOT EXISTS city" +
                "(city VARCHAR(45) NOT NULL," +
                "country VARCHAR(45) NOT NULL, " +
                "latitude VARCHAR(15) NOT NULL, " +
                "longitude VARCHAR(15) NOT NULL," +
                "PRIMARY KEY (city, country))";
        String createUserSql = "CREATE TABLE IF NOT EXISTS user" +
                "(email VARCHAR(255) PRIMARY KEY NOT NULL," +
                " name VARCHAR(1000) NOT NULL)";
        String createUserCitySql = "CREATE TABLE IF NOT EXISTS user_city" +
                "(users_email VARCHAR(255) NOT NULL, " +
                "cities_city VARCHAR(255) NOT NULL, " +
                "cities_country VARCHAR(255) NOT NULL," +
                "FOREIGN KEY(users_email) REFERENCES user(email), " +
                "FOREIGN KEY(cities_city) REFERENCES city(city)," +
                "FOREIGN KEY(cities_country) REFERENCES city(country))";
        String deleteUserSql = "DELETE FROM user";
        String deleteCitySql = "DELETE FROM city";
        String deleteUserCitySql = "DELETE FROM user_city";

        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            session.createNativeQuery(createUserCitySql, Void.class).executeUpdate();
            session.createNativeQuery(deleteUserCitySql, Void.class).executeUpdate();

            session.createNativeQuery(createUserSql, Void.class).executeUpdate();
            session.createNativeQuery(deleteUserSql, Void.class).executeUpdate();

            session.createNativeQuery(createCitySql, Void.class).executeUpdate();
            session.createNativeQuery(deleteCitySql, Void.class).executeUpdate();


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
        userDatabaseService.insert(email);

        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);
        cityDatabaseService.insert(city);

        userCityDatabaseService.addUserCity(email, city);

        Assertions.assertTrue(city.getEmails().contains(email));
        Assertions.assertTrue(email.getCities().contains(city));
    }

    @Test
    void testWithAddingNonExistentUserAndCity() {
        testWithInsertChecking();
        Email email = new Email("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);

        Email nonExistentEmail = new Email("kilogod@mail.ru", "kilogod");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.addUserCity(nonExistentEmail, city);});

        City nonExistentCity = new City(new CityPK("Vitebsk", "Belarus"), 1.0, 1.0);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.addUserCity(email, nonExistentCity);});
    }

    @Test
    public void testWithDeleteChecking() {
        testWithInsertChecking();
        Email email = new Email("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);

        userCityDatabaseService.removeUserCity(email, city);
        Assertions.assertFalse(city.getEmails().contains(email));
        Assertions.assertFalse(email.getCities().contains(city));
    }

    @Test
    void testWithRemovingNonExistentUserAndCity() {
        testWithInsertChecking();
        Email email = new Email("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);

        Email nonExistentEmail = new Email("kilogod@mail.ru", "kilogod");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.removeUserCity(nonExistentEmail, city);});

        City nonExistentCity = new City(new CityPK("Vitebsk", "Belarus"), 1.0, 1.0);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.removeUserCity(email, nonExistentCity);});
    }

    @Test
    void testWithGetUsersChecking() {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(email);
        List<City> cities = List.of(
                new City(new CityPK("Brest", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Gomel", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Vitebsk", "Belarus"), 1.0, 1.0));
        cities.forEach(cityDatabaseService::insert);

        for (int i = 0; i < cities.size(); i++) {
            userCityDatabaseService.addUserCity(email, cities.get(i));
        }

        Set<City> citySet = userCityDatabaseService.getCities(email);
        Assertions.assertEquals(citySet.size(), cities.size());

        List<City> cityList = new ArrayList<>(citySet);
        cityList.sort(Comparator.comparing((city -> city.getPk().getCity())));

        for (int i = 0; i < citySet.size(); i++) {
            Assertions.assertEquals(cities.get(i), cityList.get(i));
        }
    }

    @Test
    void testWithGetCitiesChecking() {
        List<Email> emails = List.of(
                new Email("ektobot@mail.ru", "ektogod"),
                new Email("ektodog@mail.ru", "ektogod"),
                new Email("ektogod@mail.ru", "ektogod"),
                new Email("etokto@mail.ru", "ektogod"));
        emails.forEach(userDatabaseService::insert);

        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);
        cityDatabaseService.insert(city);
        for (Email email : emails) {
            userCityDatabaseService.addUserCity(email, city);
        }

        Set<Email> emailSet = userCityDatabaseService.getUsers(city);
        Assertions.assertEquals(emailSet.size(), emails.size());

        List<Email> emailList = new ArrayList<>(emailSet);
        emailList.sort(Comparator.comparing((Email::getEmail)));

        for (int i = 0; i < emailSet.size(); i++) {
            Assertions.assertEquals(emails.get(i), emailList.get(i));
        }
    }
}
