package edu.java.dao.jooq;

import edu.java.dao.LinkUpdater;
import edu.java.dao.dto.LinkContent;
import edu.java.domain.jooq.tables.Link;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class JooqLinkUpdater implements LinkUpdater {

    private static final Integer SECONDS = 60;

    private final DSLContext dsl;

    private final Link link = Link.LINK;

    public JooqLinkUpdater(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void update(String url, String content) {
        dsl.update(link)
            .set(link.CONTENT, content)
            .set(link.CHECKED_TIME, DSL.currentOffsetDateTime())
            .where(link.URL.eq(url))
            .execute();
    }

    @Override
    public List<LinkContent> findAll(int time) {
        return dsl.select()
            .from(link)
            .where(
                link.CHECKED_TIME.lt(DSL.currentOffsetDateTime().subtract(DSL.inline(time).mul(SECONDS)))
            )
            .fetchInto(LinkContent.class);
    }
}
