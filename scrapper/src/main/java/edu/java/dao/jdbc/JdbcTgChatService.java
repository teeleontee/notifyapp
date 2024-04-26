package edu.java.dao.jdbc;

import edu.java.dao.TgChatService;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTgChatService implements TgChatService {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTgChatService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void register(long chatId) {
        String sql = """
            INSERT INTO tgchat VALUES (?)
            """;
        jdbcTemplate.update(sql, chatId);
    }

    @Override
    public void unregister(long chatId) {
        String sql = """
            DELETE FROM tgchat WHERE id = ?
            """;
        jdbcTemplate.update(sql, chatId);
    }
}
