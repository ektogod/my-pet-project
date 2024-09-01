package database;

import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.User;
import com.tinkoff_lab.exception.EntityNotFoundException;
import com.tinkoff_lab.services.database.CityDatabaseService;
import com.tinkoff_lab.services.database.UserCityDatabaseService;
import com.tinkoff_lab.services.database.UserDatabaseService;
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
        User user = new User("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(user);

        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        cityDatabaseService.insert(city);

        userCityDatabaseService.addUserCity(user, city);

        Assertions.assertTrue(city.getUsers().contains(user));
        Assertions.assertTrue(user.getCities().contains(city));
    }

    @Test
    void testWithAddingNonExistentUserAndCity() {
        testWithInsertChecking();
        User user = new User("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");

        User nonExistentUser = new User("kilogod@mail.ru", "kilogod");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.addUserCity(nonExistentUser, city);});

        City nonExistentCity = new City(new CityPK("Vitebsk", "Belarus"), "latitude", "longitude");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.addUserCity(user, nonExistentCity);});
    }

    @Test
    public void testWithDeleteChecking() {
        testWithInsertChecking();
        User user = new User("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");

        userCityDatabaseService.removeUserCity(user, city);
        Assertions.assertFalse(city.getUsers().contains(user));
        Assertions.assertFalse(user.getCities().contains(city));
    }

    @Test
    void testWithRemovingNonExistentUserAndCity() {
        testWithInsertChecking();
        User user = new User("ektogod@mail.ru", "ektogod");
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");

        User nonExistentUser = new User("kilogod@mail.ru", "kilogod");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.removeUserCity(nonExistentUser, city);});

        City nonExistentCity = new City(new CityPK("Vitebsk", "Belarus"), "latitude", "longitude");
        Assertions.assertThrows(EntityNotFoundException.class, () -> {userCityDatabaseService.removeUserCity(user, nonExistentCity);});
    }

    @Test
    void testWithGetUsersChecking() {
        User user = new User("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(user);
        List<City> cities = List.of(
                new City(new CityPK("Brest", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Gomel", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Vitebsk", "Belarus"), "latitude", "longitude"));
        cities.forEach(cityDatabaseService::insert);

        for (int i = 0; i < cities.size(); i++) {
            userCityDatabaseService.addUserCity(user, cities.get(i));
        }

        List<City> userCities = userCityDatabaseService.getCities(user);
        Assertions.assertEquals(userCities.size(), cities.size());
        userCities.sort(Comparator.comparing((city -> city.getPk().getCity())));

        for (int i = 0; i < userCities.size(); i++) {
            Assertions.assertEquals(cities.get(i), userCities.get(i));
        }
    }

    @Test
    void testWithGetCitiesChecking() {
        List<User> users = List.of(
                new User("ektobot@mail.ru", "ektogod"),
                new User("ektodog@mail.ru", "ektogod"),
                new User("ektogod@mail.ru", "ektogod"),
                new User("etokto@mail.ru", "ektogod"));
        users.forEach(userDatabaseService::insert);

        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        cityDatabaseService.insert(city);
        for (User user : users) {
            userCityDatabaseService.addUserCity(user, city);
        }

        List<User> cityUsers = userCityDatabaseService.getUsers(city);
        Assertions.assertEquals(cityUsers.size(), users.size());
        cityUsers.sort(Comparator.comparing((User::getEmail)));

        for (int i = 0; i < cityUsers.size(); i++) {
            Assertions.assertEquals(users.get(i), cityUsers.get(i));
        }
    }
}
