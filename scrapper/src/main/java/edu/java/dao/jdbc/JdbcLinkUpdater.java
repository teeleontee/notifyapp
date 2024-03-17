package edu.java.dao.jdbc;

import edu.java.dao.LinkUpdater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JdbcLinkUpdater implements LinkUpdater {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void update(String link, String content) {
        String sql = """
            UPDATE link
            SET checked_time = NOW()
            WHERE url = ?
            SET content = ?
            """;
        jdbcTemplate.update(sql, link, content);
    }
}
