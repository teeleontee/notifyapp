package edu.java.scrapper.service;

import edu.java.dao.LinkService;
import edu.java.dao.TgChatService;
import edu.java.dao.dto.Link;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public abstract class ServiceTest extends IntegrationTest {

    protected final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    @BeforeAll
    public static void setup() {
        dataSource = new DriverManagerDataSource(
            POSTGRES.getJdbcUrl(),
            POSTGRES.getUsername(),
            POSTGRES.getPassword()
        );
    }

    protected abstract TgChatService getTgChatService();

    protected abstract LinkService getLinkService();

    protected abstract Long getProfile();

    protected abstract List<URI> getTestCases();

    @Test
    public void chatRegisterAndUnregisterTest() {
        var tgChatService = getTgChatService();
        long tgchatId = getProfile();
        tgChatService.register(tgchatId);
        String sql = String.format("""
            SELECT * FROM tgchat WHERE id = %d
            """, getProfile());
        String received1 = jdbcTemplate.query(sql, resultSet -> {
            resultSet.next();
            return resultSet.getString("id");
        });
        Assertions.assertEquals(getProfile().toString(), received1);
        tgChatService.unregister(tgchatId);
        List<String> received2 = jdbcTemplate.query(sql, (resultSet, i) -> {
            return resultSet.getString("id");
        });
        Assertions.assertEquals(0, received2.size());
    }

    @Test
    public void addAndRemoveLinkTest() {
        var tgChatService = getTgChatService();
        var linkService = getLinkService();
        var chatId = getProfile();

        URI uri = getTestCases().getFirst();
        tgChatService.register(chatId);
        linkService.add(chatId, uri);
        List<Link> received1 = linkService.listAll(chatId);
        Assertions.assertEquals(1, received1.size());

        System.err.println("chat_id : " + chatId);
        String sql = """
            SELECT * FROM link
            """;

        List<String> strs = jdbcTemplate.query(sql, (resultSet, i) -> {
           return resultSet.getString("id") + " " + resultSet.getString("url");
        });

        System.err.println("link");
        for (var str : strs) {
            System.err.println(str);
        }
    }

    @Test
    public void listAllTest() {
        var tgChatService = getTgChatService();
        var linkService = getLinkService();
        var chatId = getProfile();

        List<URI> uris = getTestCases();
        tgChatService.register(chatId);
        for (URI uri : uris) {
            linkService.add(chatId, uri);
        }

        List<Link> received1 = linkService.listAll(chatId);
        Assertions.assertEquals(uris.size(), received1.size());

        for (URI uri : uris) {
            linkService.remove(chatId, uri);
        }
        tgChatService.unregister(chatId);

        System.err.println("chat_id : " + chatId);
        String sql = """
            SELECT * FROM link
            """;

        List<String> strs = jdbcTemplate.query(sql, (resultSet, i) -> {
            return resultSet.getString("id") + " " + resultSet.getString("url");
        });

        System.err.println("link");
        for (var str : strs) {
            System.err.println(str);
        }
    }
}
