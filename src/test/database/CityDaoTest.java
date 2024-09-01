package database;

import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.exception.DatabaseException;
import com.tinkoff_lab.services.database.CityDatabaseService;
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
public class CityDaoTest {
    private final CityDatabaseService databaseService;
    private final SessionFactory sessionFactory;

    @Container
    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("tinkoff_lab_db")
            .withUsername("root")
            .withPassword("sqlkilogodH19");

    @Autowired
    public CityDaoTest(CityDatabaseService databaseService, SessionFactory sessionFactory) {
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
        String createSql = "CREATE TABLE IF NOT EXISTS city" +
                "(city VARCHAR(45) NOT NULL," +
                "country VARCHAR(45) NOT NULL, " +
                "latitude VARCHAR(15) NOT NULL, " +
                "longitude VARCHAR(15) NOT NULL," +
                "PRIMARY KEY (city, country))";
        String deleteSql = "DELETE FROM city";
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
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        databaseService.insert(city);

        String createSql = "SELECT * FROM city";
        List<City> cities = null;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        try {
            transaction.begin();
            cities = session.createNativeQuery(createSql, City.class).list(); // I use this query because I dont want to use another function from service in this method. I decided firstly ensure that at least one method works correct
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.getStatus() == TransactionStatus.ACTIVE || transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        Assertions.assertNotNull(cities);
        Assertions.assertEquals(cities.size(), 1);
        Assertions.assertEquals(cities.get(0), city);

        Assertions.assertThrows(DatabaseException.class, // trying to add duplicate
                () -> databaseService.insert(new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude")));
    }

    @Test
    public void testWithFindByIdChecking() {
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        databaseService.insert(city);
        Assertions.assertEquals(city, databaseService.findByID(city.getPk()));

        Assertions.assertNull(databaseService.findByID(new CityPK("aa", "asa"))); // trying to find non-existent note
    }

    @Test
    public void testWithFindAllChecking() {
        List<City> cities = List.of(
                new City(new CityPK("Brest", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Gomel", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude"),
                new City(new CityPK("Vitebsk", "Belarus"), "latitude", "longitude"));
        cities.forEach(databaseService::insert);

        List<City> dbCities = databaseService.findAll(); // order in returned list was different with "cities" order, so I decided to add cities by alphabet order and then sort them
        Assertions.assertEquals(cities.size(), dbCities.size());
        dbCities.sort(Comparator.comparing(c -> c.getPk().getCity()));

        for (int i = 0; i < cities.size(); i++) {
            Assertions.assertEquals(cities.get(i), dbCities.get(i));
        }
    }

    @Test
    public void testWithUpdateChecking() {
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        databaseService.update(city); // use update instead of insert to check what happens if we try to update non-existent note

        City updatedCity = new City(new CityPK("Minsk", "Belarus"), "newLatitude", "newLongitude");
        databaseService.update(updatedCity);
        Assertions.assertEquals(updatedCity, databaseService.findByID(city.getPk()));
    }

    @Test
    public void testWithDeleteChecking() {
        City city = new City(new CityPK("Minsk", "Belarus"), "latitude", "longitude");
        databaseService.insert(city);

        List<City> dbCities = databaseService.findAll();
        Assertions.assertEquals(dbCities.size(), 1);

        databaseService.delete(city.getPk());
        dbCities = databaseService.findAll();
        Assertions.assertEquals(dbCities.size(), 0);

        Assertions.assertThrows(DatabaseException.class,
                () -> databaseService.delete(new CityPK("wew", "ds")));// trying to delete non-existent note
    }
}
