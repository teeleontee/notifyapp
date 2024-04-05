package edu.java.scrapper.service;

import edu.java.dao.LinkService;
import edu.java.dao.TgChatService;
import edu.java.dao.jdbc.JdbcLinkService;
import edu.java.dao.jdbc.JdbcTgChatService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.net.URI;
import java.util.List;

@SpringBootTest
@TestPropertySource(properties = "app.connection-type=jdbc")
public class JdbcServiceTest extends ServiceTest {

    @Override
    protected TgChatService getTgChatService() {
        return new JdbcTgChatService(jdbcTemplate);
    }

    @Override
    protected LinkService getLinkService() {
        return new JdbcLinkService(jdbcTemplate);
    }

    @Override
    protected Long getProfile() {
        return 19231L;
    }

    @Override
    protected List<URI> getTestCases() {
        return List.of(
            URI.create("https://github.com/arrow-kt/arrow"),
            URI.create("https://github.com/mockito/mockito-kotlin"),
            URI.create("https://github.com/usebruno/bruno")
        );
    }
}
