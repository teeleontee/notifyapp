package edu.java.configuration;

import edu.java.dao.jdbc.JdbcLinkService;
import edu.java.dao.jdbc.JdbcLinkUpdater;
import edu.java.dao.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "connection-type", havingValue = "jdbc")
public class JdbcConfig {

    private final JdbcTemplate jdbcTemplate;

    public JdbcConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public JdbcLinkService jdbcLinkService() {
        return new JdbcLinkService(jdbcTemplate);
    }

    @Bean
    public JdbcLinkUpdater jdbcLinkUpdater() {
        return new JdbcLinkUpdater(jdbcTemplate);
    }

    @Bean
    public JdbcTgChatService tgChatService() {
        return new JdbcTgChatService(jdbcTemplate);
    }
}
