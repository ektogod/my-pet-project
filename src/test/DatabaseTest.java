package test;

import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.dto.Translation;
import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.services.ConnectionService;
import com.tinkoff_lab.services.database.TranslationDatabaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest(classes = TinkoffLabApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc

public class DatabaseTest {
    private final ConnectionService connectionService;
    private final TranslationDatabaseService databaseService;
    private final MockMvc mockMvc;

    @Autowired
    public DatabaseTest(ConnectionService connectionService, TranslationDatabaseService databaseService, MockMvc mockMvc) {
        this.connectionService = connectionService;
        this.databaseService = databaseService;
        this.mockMvc = mockMvc;
    }

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("database.url", mySQLContainer::getJdbcUrl);
        registry.add("database.username", mySQLContainer::getUsername);
        registry.add("database.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        try (Connection connection = connectionService.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS query (" +
                    "  ID INT NOT NULL AUTO_INCREMENT," +
                    "  IP VARCHAR(45) NOT NULL," +
                    "  Original_Text VARCHAR(1000)," +
                    "  Original_Language VARCHAR(10)," +
                    "  Translated_Text VARCHAR(1000)," +
                    "  Target_Language VARCHAR(10)," +
                    "  Time DATETIME NOT NULL," +
                    "  Status INT NOT NULL," +
                    "  Message VARCHAR(1000)," +
                    "  PRIMARY KEY (ID)" +
                    ")");

            statement.execute("DELETE FROM query");
        }
    }

    @Test
    public void testWhenQueryIsCorrect() {
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        int id = databaseService.insert(translation);
        Assertions.assertEquals(translation, databaseService.findByID(id));
    }

    @Test
    public void testWhenQueryHasNullColumns() {
        Translation translation = new Translation(
                null,
                null,
                null,
                null,
                null,
                null,
                200,
                "Ok");

        final int[] id = new int[1]; // this variant was advised by IDE
        Exception ex = Assertions.assertThrows(DatabaseException.class, () -> {
            id[0] = databaseService.insert(translation);
        });

        Assertions.assertEquals(ex.getMessage(), "Insertion of query in database goes wrong!");
        Assertions.assertNull(databaseService.findByID(id[0]));
    }

    @Test
    public void testWithSomeQueries() {
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        for (int i = 0; i < 5; i++) {
            int id = databaseService.insert(translation);
            Assertions.assertEquals(translation, databaseService.findByID(id));
        }
    }

    @Test
    public void testWhenTextIsLargerThan1000(){
        Translation translation = new Translation(
                "ip",
                "a".repeat(1001),
                "ru",
                "a".repeat(1001),
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        final int[] id = new int[1];
        Exception ex = Assertions.assertThrows(DatabaseException.class, () ->{
            id[0] = databaseService.insert(translation);
        });

        Assertions.assertEquals(ex.getMessage(), "Insertion of query in database goes wrong!");
        Assertions.assertNull(databaseService.findByID(id[0]));
    }

    @Test
    public void testWithDeleteChecking(){
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        int id = databaseService.insert(translation);
        databaseService.delete(id);

        Assertions.assertNull(databaseService.findByID(id));
        Assertions.assertNull(databaseService.findByID(id + 1)); //checking case when there is nothing to delete
    }

    @Test
    public void testWithFindAllChecking(){
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ids.add(databaseService.insert(translation));
        }

        List<Translation> entities = databaseService.findAll();
        Assertions.assertEquals(entities.size(), 5);
        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(translation, databaseService.findByID(ids.get(i)));
        }
    }

    @Test
    public void testWhenUpdateChecking(){
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        int id = databaseService.insert(translation);
        Translation newTranslation = new Translation(
                "ip",
                "new_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:54:18",
                200,
                "Ok");

        databaseService.update(id, newTranslation);
        Assertions.assertEquals(newTranslation, databaseService.findByID(id));
    }

    @Test
    public void testWhenFindByIdChecking(){
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        int id = databaseService.insert(translation);
        Assertions.assertEquals(translation, databaseService.findByID(id));
    }

}
