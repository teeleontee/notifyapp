package edu.java.dao.jooq;

import edu.java.dao.TgChatService;
import edu.java.domain.jooq.tables.Tgchat;
import org.jooq.DSLContext;

public class JooqTgChatService implements TgChatService {

    private final DSLContext dsl;

    private final Tgchat tgchat = Tgchat.TGCHAT;

    public JooqTgChatService(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void register(long chatId) {
        dsl.insertInto(tgchat).set(tgchat.ID, chatId).execute();
    }

    @Override
    public void unregister(long chatId) {
        dsl.deleteFrom(tgchat).where(tgchat.ID.eq(chatId)).execute();
    }
}
