package edu.java.scrapper.service;

import edu.java.dao.LinkService;
import edu.java.dao.TgChatService;
import edu.java.dao.jdbc.JdbcLinkService;
import edu.java.dao.jdbc.JdbcTgChatService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JdbcServiceTest extends ServiceTest {

    @Override
    protected TgChatService getTgChatService() {
        return new JdbcTgChatService(jdbcTemplate);
    }

    @Override
    protected LinkService getLinkService() {
        return new JdbcLinkService(jdbcTemplate);
    }
}
