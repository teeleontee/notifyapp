package edu.java.dao.jdbc;

import edu.java.dao.LinkUpdater;
import edu.java.dao.dto.LinkContent;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
public class JdbcLinkUpdater implements LinkUpdater {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void update(String link, String content) {
        try {
            String sql = String.format("""
                UPDATE link
                SET checked_time = NOW(),
                    content = '%s'
                WHERE url = '%s'
                """, content, link);
            jdbcTemplate.update(sql);
        } catch (final DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<LinkContent> findAll(int time) {
        String sql = String.format("""
            SELECT * FROM link
            WHERE checked_time < now() - Interval '%d minute'
            """, time);
        try {
            return jdbcTemplate.query(sql, (resultSet, i) -> {
                try {
                    return new LinkContent(
                        URI.create(resultSet.getString("url")),
                        resultSet.getString("content")
                    );
                } catch (final IllegalArgumentException e) {
                    // should never happen, because database contains
                    // only valid uri's
                    log.debug("Unable to create URI");
                    return null;
                }
            });
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
