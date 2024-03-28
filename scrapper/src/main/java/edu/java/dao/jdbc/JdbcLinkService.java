package edu.java.dao.jdbc;

import edu.java.dao.LinkService;
import edu.java.dao.dto.Link;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JdbcLinkService implements LinkService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(long tgChatId, URI url) {
        String sql = String.format("""
            WITH inserted_link AS (
              INSERT INTO link (url, checked_time)
              VALUES ('%s', NOW())
              RETURNING id
            )
            INSERT INTO task (chat_id, link_id)
            SELECT %d, id FROM inserted_link;
            """, url, tgChatId);
        try {
            jdbcTemplate.update(sql);
        } catch (DataAccessException e) {
            System.err.println("da exception" + e.getMessage());
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        String sql = String.format("""
            DELETE FROM task WHERE chat_id = %d
                AND link_id = (SELECT id FROM link WHERE url = '%s')
            """, tgChatId, url);
        jdbcTemplate.update(sql);
    }

    @Override
    public List<Link> listAll(long tgChatId) {
        String sql = String.format("""
            SELECT l.url FROM task t
            JOIN link l ON t.link_id = l.id
            WHERE t.chat_id = %d
            """, tgChatId);
        try {
            return jdbcTemplate.query(sql, (resultSet, i) -> {
                try {
                    return new Link(
                        URI.create(resultSet.getString("url"))
                    );
                } catch (final IllegalArgumentException e) {
                    log.debug("Unable to create URI");
                    // should never happen, because database contains
                    // only valid uri's

                    return null;
                }
            });
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}
