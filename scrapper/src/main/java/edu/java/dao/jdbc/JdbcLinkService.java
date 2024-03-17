package edu.java.dao.jdbc;

import edu.java.dao.LinkService;
import edu.java.dao.dto.Link;
import java.net.URI;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JdbcLinkService implements LinkService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public boolean add(long tgChatId, URI url) {
        try {
            String sql = """
                WITH inserted_link AS (
                  INSERT INTO link (url, checked_time)
                  VALUES (?, NOW())
                  RETURNING id
                )
                INSERT INTO task (chat_id, link_id)
                SELECT ?, id FROM inserted_link;
                """;
            jdbcTemplate.update(sql, url, tgChatId);
        } catch (final DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean remove(URI url) {
        try {
            String sql = """
            DELETE FROM link WHERE url = ?
            """;
            jdbcTemplate.update(sql, url);
        } catch (final DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public List<Link> listAll(long tgChatId) {
        String sql = String.format("""
            SELECT l.url FROM task t
            JOIN link l ON t.link_id = l.id
            WHERE t.chat_id = %s
            """, tgChatId);
        return jdbcTemplate.query(sql, (resultSet, i) -> new Link(
            resultSet.getString("url")
        ));
    }
}
