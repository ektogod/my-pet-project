package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.email_sender.EmailSenderApplication;
import com.tinkoff_lab.dto.weather.CityDTO;
import com.tinkoff_lab.dto.weather.request.email.EmailCitiesRequest;
import com.tinkoff_lab.dto.weather.request.email.EmailRequest;
import com.tinkoff_lab.dto.weather.request.email.WeatherEmailRequest;
import com.tinkoff_lab.entity.City;
import com.tinkoff_lab.entity.CityPK;
import com.tinkoff_lab.entity.Email;
import com.tinkoff_lab.service.database.CityDatabaseService;
import com.tinkoff_lab.service.database.UserCityDatabaseService;
import com.tinkoff_lab.service.database.UserDatabaseService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@SpringBootTest(classes = EmailSenderApplication.class)
@TestPropertySource(locations = "classpath:hibernate.properties")
@AutoConfigureMockMvc
@Testcontainers
@Transactional

public class WeatherControllerTest {
    private final MockMvc mockMvc;
    private final SessionFactory sessionFactory;
    private final CityDatabaseService cityDatabaseService;
    private final UserDatabaseService userDatabaseService;
    private final UserCityDatabaseService userCityDatabaseService;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public WeatherControllerTest(MockMvc mockMvc, SessionFactory sessionFactory, CityDatabaseService cityDatabaseService, UserDatabaseService userDatabaseService, UserCityDatabaseService userCityDatabaseService) {
        this.mockMvc = mockMvc;
        this.sessionFactory = sessionFactory;
        this.cityDatabaseService = cityDatabaseService;
        this.userDatabaseService = userDatabaseService;
        this.userCityDatabaseService = userCityDatabaseService;
    }

    private static final MySQLContainer<?> container = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("tinkoff_lab_db")
            .withUsername("root")
            .withPassword("sqlkilogodH19");

    @BeforeAll
    public static void set() {
        container.start();
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
    void testWithSubscribeChecking() throws Exception {
        List<CityDTO> cities = List.of(new CityDTO("Minsk", "Belarus"), new CityDTO("Grodno", "Belarus"));
        WeatherEmailRequest request = new WeatherEmailRequest("ektogod@gmail.com", "ektogod", cities);
        post(request, "", 201);

        Email email = new Email("ektogod@gmail.com", "ektogod");
        Email dbEmail = userDatabaseService.findByID(email.getEmail());
        Assertions.assertEquals(email, dbEmail);

        Assertions.assertEquals(dbEmail.getCities().size(), 2);
    }

    @Test
    void testWithGetCitiesChecking() throws Exception {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(email);
        List<City> cities = List.of(
                new City(new CityPK("Grodno", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0));
        cities.forEach(cityDatabaseService::insert);

        for (int i = 0; i < cities.size(); i++) {
            userCityDatabaseService.addUserCity(email, cities.get(i));
        }

        EmailRequest request = new EmailRequest("ektogod@mail.ru");
        String response = "[{\"city\":\"Grodno\",\"country\":\"Belarus\"},{\"city\":\"Minsk\",\"country\":\"Belarus\"}]";
        get(request, response, 200);
    }

    @Test
    void testWithUnsubscribeChecking() throws Exception {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(email);
        List<City> cities = List.of(
                new City(new CityPK("Grodno", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0));
        cities.forEach(cityDatabaseService::insert);

        for (int i = 0; i < cities.size(); i++) {
            userCityDatabaseService.addUserCity(email, cities.get(i));
        }

        EmailRequest request = new EmailRequest("ektogod@mail.ru");
        delete(request, "", 200);
    }

    @Test
    void testWithUpdateChecking() throws Exception {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(email);

        City city = new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0);
        cityDatabaseService.insert(city);

        userCityDatabaseService.addUserCity(email, city);

        List<CityDTO> cities = List.of(
                new CityDTO("Grodno", "Belarus"),
                new CityDTO("Minsk", "Belarus"));
        EmailCitiesRequest request = new EmailCitiesRequest("ektogod@mail.ru", cities);
        put(request, "", 200);

        Assertions.assertEquals(userDatabaseService.findByID("ektogod@mail.ru").getCities().size(), 2);
    }

    @Test
    void testWithDeleteCitiesChecking() throws Exception {
        Email email = new Email("ektogod@mail.ru", "ektogod");
        userDatabaseService.insert(email);
        List<City> cities = List.of(
                new City(new CityPK("Grodno", "Belarus"), 1.0, 1.0),
                new City(new CityPK("Minsk", "Belarus"), 1.0, 1.0));
        cities.forEach(cityDatabaseService::insert);

        for (int i = 0; i < cities.size(); i++) {
            userCityDatabaseService.addUserCity(email, cities.get(i));
        }

        List<CityDTO> cityDTOS = List.of(new CityDTO("Minsk", "Belarus"));
        EmailCitiesRequest request = new EmailCitiesRequest(email.getEmail(), cityDTOS);
        deleteCities(request, "", 200);

        Assertions.assertEquals(userDatabaseService.findByID("ektogod@mail.ru").getCities().size(), 1);
    }

    @Test
    void testWithEntityNotFoundChecking() throws Exception {
        EmailRequest emailRequest = new EmailRequest("ektogod@mail.ru");
        EmailCitiesRequest emailCitiesRequest = new EmailCitiesRequest("ektogod@mail.ru", List.of(new CityDTO("city", "country")));

        get(emailRequest, "User not found", 400);
        delete(emailRequest, "User not found", 400);
        deleteCities(emailCitiesRequest, "User not found", 400);
        put(emailCitiesRequest, "User not found", 400);

        userDatabaseService.insert(new Email("ektogod@mail.ru", "ektogod"));
        deleteCities(emailCitiesRequest, "City not found", 400);
    }

    @Test
    void testWithWrongWeatherRequestChecking() throws Exception {
        List<CityDTO> citiesWithWrongCountry = List.of(new CityDTO("Minsk", "Belraus"));
        WeatherEmailRequest request = new WeatherEmailRequest("ektogod@mail.ru", "ektogod", citiesWithWrongCountry);
        post(request, "Something wrong with country: code not defined", 400);

        List<CityDTO> citiesWithWrongLocation = List.of(new CityDTO("Asjlas", "Belarus"));
        WeatherEmailRequest request2 = new WeatherEmailRequest("ektodog@mail.ru", "ektogod", citiesWithWrongLocation);
        post(request2, "Something wrong with location. Coordinates not defined", 400);
    }

    void post(WeatherEmailRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ektogod/weather/subscribe")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    void get(EmailRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ektogod/weather/get")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    void delete(EmailRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ektogod/weather/unsubscribe")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    void deleteCities(EmailCitiesRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ektogod/weather/delete")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }

    void put(EmailCitiesRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/ektogod/weather/add")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }
}
