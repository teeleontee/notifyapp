package edu.java;

import edu.java.clients.BotClient;
import edu.java.clients.GithubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.clients.details.LinkUpdateRequest;
import edu.java.dao.LinkService;
import edu.java.dao.LinkUpdater;
import edu.java.dao.dto.LinkContent;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    private final int defaultInterval = 10;

    private final GithubClient githubClient;
    private final StackOverflowClient stackOverflowClient;
    private final LinkUpdater linkUpdater;
    private final LinkService linkService;
    private final BotClient botClient;

    public LinkUpdaterScheduler(
        GithubClient client,
        StackOverflowClient stackOverflowClient,
        LinkUpdater linkUpdater,
        LinkService linkService,
        BotClient botClient
    ) {
        this.githubClient = client;
        this.stackOverflowClient = stackOverflowClient;
        this.linkUpdater = linkUpdater;
        this.linkService = linkService;
        this.botClient = botClient;
    }

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    void update() {
        log.debug("scheduled");
        List<LinkContent> links = linkUpdater.findAll(defaultInterval);
        links.forEach(link -> log.debug(link.url() + " -- needs updating"));
        for (var link : links) {
            String details = null;
            if (GithubClient.isValidGithubUrl(link.url())) {
                details = Objects.requireNonNull(githubClient.getGithubInfoByUri(link.url()).block())
                    .toString();
            } else if (StackOverflowClient.isValidStackOverflowUri(link.url())) {
                details = Objects.requireNonNull(stackOverflowClient.getQuestionInfoByUri(link.url()).block())
                    .toString();
            }
            if (!Objects.equals(details, link.content())) {
                linkUpdater.update(link.url().toString(), details);
                List<Long> ids = linkService.listAllTgChatsWithLink(link.url());
                botClient.update(
                        new LinkUpdateRequest(
                            0,
                            link.url(),
                            "link has been updated",
                            ids
                        )).subscribe()
                    .dispose();
            }
        }
    }
}
