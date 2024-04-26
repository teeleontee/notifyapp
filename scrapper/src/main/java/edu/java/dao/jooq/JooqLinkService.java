package edu.java.dao.jooq;

import edu.java.dao.LinkService;
import edu.java.domain.jooq.tables.Link;
import edu.java.domain.jooq.tables.Task;
import edu.java.domain.jooq.tables.Tgchat;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

@Slf4j
public class JooqLinkService implements LinkService {

    private final DSLContext dsl;

    private final Tgchat tgchat = Tgchat.TGCHAT;

    private final Link link = Link.LINK;

    private final Task task = Task.TASK;

    public JooqLinkService(DSLContext dsl) {
        this.dsl = dsl;
    }

    private Long getIdFromUrl(URI url) {
        var result = dsl.select(link.ID.cast(Long.class)).where(link.URL.eq(url.toString())).fetchInto(Long.class);
        return result.getFirst();
    }

    private List<Long> getIdsFromTasK(Long linkId) {
        return dsl.select(
            link.ID.cast(Long.class)).where(link.ID.eq(linkId.intValue())
        ).fetchInto(Long.class);
    }

    @Override
    public void add(long tgChatId, URI url) {
        Long linkId = getIdFromUrl(url);
        if (linkId == null) {
            var insertedLinkId = Objects.requireNonNull(dsl.insertInto(link)
                .set(link.URL, url.toString())
                .set(link.CHECKED_TIME, DSL.currentOffsetDateTime())
                .returning(link.ID)
                .fetchOne());
            dsl.insertInto(task).columns(task.CHAT_ID, task.LINK_ID)
                .values(tgChatId, Objects.requireNonNull(insertedLinkId.getId()).longValue())
                .execute();
        } else {
            dsl.insertInto(task).columns(task.CHAT_ID, task.LINK_ID)
                .values(tgChatId, linkId).execute();
        }
    }

    @Override
    public void remove(long tgChatId, URI url) {
        Long linkId = getIdFromUrl(url);
        dsl.deleteFrom(task)
            .where(task.CHAT_ID.eq(tgChatId).and(task.LINK_ID.eq(linkId)))
            .execute();
        var ids = getIdsFromTasK(linkId);
        if (ids.isEmpty()) {
            dsl.deleteFrom(link)
                .where(link.ID.eq(linkId.intValue()))
                .execute();
        }
    }

    @Override
    public List<edu.java.dao.dto.Link> listAll(long tgChatId) {
        return dsl.select(link.URL)
            .from(task)
            .join(link).on(task.LINK_ID.eq(link.ID.cast(Long.class)))
            .where(task.CHAT_ID.eq(tgChatId))
            .fetchInto(edu.java.dao.dto.Link.class);
    }

    @Override
    public List<Long> listAllTgChatsWithLink(URI url) {
        Long id = getIdFromUrl(url);
        return getIdsFromTasK(id);
    }
}
