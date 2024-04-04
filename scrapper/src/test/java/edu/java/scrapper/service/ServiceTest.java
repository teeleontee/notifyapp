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

    @Test
    public void chatRegisterAndUnregisterTest() {
        var tgChatService = getTgChatService();
        long tgchatId = 111L;
        tgChatService.register(tgchatId);
        String sql = """
            SELECT * FROM tgchat WHERE id = 111
            """;
        String received1 = jdbcTemplate.query(sql, (resultSet) -> {
            resultSet.next();
            return resultSet.getString("id");
        });
        Assertions.assertEquals("111", received1);
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
        long chatId = 111L;
        tgChatService.register(chatId);
        URI githubUri = URI.create("https://github.com/");
        linkService.add(chatId, githubUri);

        String sql1 = """
            SELECT * FROM link WHERE url = 'https://github.com/'
            """;
        String linkId = jdbcTemplate.query(sql1, (resultSet) -> {
            resultSet.next();
            return resultSet.getString("id");
        });

        String sql2 = String.format("""
            SELECT * FROM task WHERE link_id = %s
            """, linkId);
        String chatRefForLink = jdbcTemplate.query(sql2, resultSet -> {
            resultSet.next();
            return resultSet.getString("chat_id");
        });

        Assertions.assertEquals("111", chatRefForLink);

        linkService.remove(chatId, githubUri);
        List<String> result = jdbcTemplate.query(sql2, (resultSet, i) -> {
            return resultSet.getString("url");
        });
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void listAllTest() {
        var tgChatService = getTgChatService();
        var linkService = getLinkService();
        tgChatService.register(1);
        tgChatService.register(2);
        linkService.add(1, URI.create("https://www.youtube.com"));
        linkService.add(2, URI.create("https://www.github.com"));
        linkService.add(2, URI.create("https://www.youtube.com"));

        List<Link> received1 = linkService.listAll(1);
        List<Link> received2 = linkService.listAll(2);

        Assertions.assertEquals(1, received1.size());
        Assertions.assertEquals(2, received2.size());
    }
}
