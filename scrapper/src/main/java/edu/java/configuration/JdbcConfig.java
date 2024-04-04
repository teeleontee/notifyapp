package edu.java.configuration;

import edu.java.dao.jdbc.JdbcLinkService;
import edu.java.dao.jdbc.JdbcLinkUpdater;
import edu.java.dao.jdbc.JdbcTgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "connection-type", havingValue = "jdbc")
public class JdbcConfig {

    @Bean
    public JdbcLinkService jdbcLinkService(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkService(jdbcTemplate);
    }

    @Bean
    public JdbcLinkUpdater jdbcLinkUpdater(JdbcTemplate jdbcTemplate) {
        return new JdbcLinkUpdater(jdbcTemplate);
    }

    @Bean
    public JdbcTgChatService tgChatService(JdbcTemplate jdbcTemplate) {
        return new JdbcTgChatService(jdbcTemplate);
    }
}
