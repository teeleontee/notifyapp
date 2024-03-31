package edu.java.dao.jpa;

import edu.java.dao.LinkUpdater;
import edu.java.dao.dto.LinkContent;
import edu.java.dao.jpa.entities.Link;
import edu.java.dao.jpa.repos.LinkRepository;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaLinkUpdater implements LinkUpdater {

    private final long minute = 60L;
    private final LinkRepository linkRepository;

    public JpaLinkUpdater(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public void update(String link, String content) {
        Optional<Link> url = linkRepository.findByUrl(link);
        url.ifPresent(it -> it.setContent(content));
        url.ifPresent(linkRepository::save);
    }

    @Override
    public List<LinkContent> findAll(int time) {
        var date = Date.from(Instant.now().minusSeconds(minute * time));
        return linkRepository.findAllByCheckedTimeBefore(date)
            .stream()
            .map(it -> new LinkContent(URI.create(it.getUrl()), it.getContent()))
            .toList();
    }
}
