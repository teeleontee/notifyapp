package edu.java.scrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
public class SmokeIntegrationTest extends IntegrationTest {
    @Test
    public void checkThatExists() {
        try (Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        )) {
            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery("SELECT COUNT(*) FROM pg_tables WHERE tablename = 'tgchat'");
            res.next();
            Assertions.assertEquals(1, res.getInt(1));
        } catch (Exception e) {
            log.error(e.getMessage());
            Assertions.fail();
        }
    }
}
