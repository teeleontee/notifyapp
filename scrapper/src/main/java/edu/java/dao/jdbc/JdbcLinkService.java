package edu.java.dao.jdbc;

import edu.java.dao.LinkService;
import edu.java.dao.dto.Link;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class JdbcLinkService implements LinkService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLinkService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Long getIdFromUrl(URI url) {
        String checkIfExistsSql = String.format("""
            SELECT id FROM link WHERE url = '%s'
            """, url);
        return jdbcTemplate.query(checkIfExistsSql, result -> {
            if (result.next()) {
                return result.getLong("id");
            }
            return null;
        });
    }

    private List<Long> getIdsFromTask(Long linkId) {
        String idsWithLinkSql = String.format("""
            SELECT * FROM task WHERE link_id = %d
            """, linkId);
        return jdbcTemplate.query(idsWithLinkSql, (resultSet, i) -> {
            return resultSet.getLong("chat_id");
        });
    }

    @Override
    public void add(long tgChatId, URI url) {
        try {
            Long linkId = getIdFromUrl(url);

            if (linkId == null) {
                String sql = String.format("""
                    WITH inserted_link AS (
                    INSERT INTO link (url, checked_time)
                    VALUES ('%s', NOW())
                    RETURNING id
                    )
                    INSERT INTO task (chat_id, link_id)
                    SELECT %d, id FROM inserted_link;
                """, url, tgChatId);
                jdbcTemplate.update(sql);
            } else {
                String addToTask = String.format("""
                INSERT INTO task (chat_id, link_id) VALUES (%d, %d)
                """, tgChatId, linkId);
                jdbcTemplate.update(addToTask);
            }
        } catch (final DataAccessException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        try {
            Long linkId = getIdFromUrl(url);

            String deleteFromTaskSql = String.format("""
            DELETE FROM task WHERE chat_id = %d AND link_id = %d
            """, tgChatId, linkId);
            jdbcTemplate.update(deleteFromTaskSql);

            var ids = getIdsFromTask(linkId);
            if (ids.isEmpty()) {
                String deleteFromLinksSql = String.format("""
                DELETE FROM link WHERE id = %d
                """, linkId);
                jdbcTemplate.update(deleteFromLinksSql);
            }
        } catch (final DataAccessException e) {
            log.error(e.getMessage());
        }
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
                    log.error("Unable to create URI");
                    // should never happen, because database contains
                    // only valid uri's
                    return null;
                }
            });
        } catch (DataAccessException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public List<Long> listAllTgChatsWithLink(URI url) {
        try {
            String sql1 = String.format("""
                SELECT * FROM link WHERE url = '%s'
                """, url.toString());
            Long id = jdbcTemplate.query(sql1, (resultSet) -> {
                resultSet.next();
                return resultSet.getLong("id");
            });
            return getIdsFromTask(id);
        } catch (final DataAccessException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
