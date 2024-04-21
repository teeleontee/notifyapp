package edu.java;

import edu.java.clients.BotClient;
import edu.java.clients.GithubClient;
import edu.java.clients.StackOverflowClient;
import edu.java.clients.details.GithubCommitInfo;
import edu.java.clients.details.LinkUpdateRequest;
import edu.java.clients.details.StackOverflowAnswersResponse;
import edu.java.dao.LinkService;
import edu.java.dao.LinkUpdater;
import edu.java.dao.dto.LinkContent;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private static final int DEFAULT_INTERVAL = 10;

    private final GithubClient githubClient;

    private final StackOverflowClient stackOverflowClient;

    private final LinkUpdater linkUpdater;

    private final LinkService linkService;

    private final BotClient botClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    void update() {
        log.debug("scheduled");
        List<LinkContent> links = linkUpdater.findAll(DEFAULT_INTERVAL);
        links.forEach(link -> log.debug("{} -- needs updating", link.url()));
        for (var link : links) {
            boolean shouldUpdate = false;
            if (GithubClient.isValidGithubUrl(link.url())) {
                GithubCommitInfo[] details = githubClient.getGithubRepoCommitInfoByUri(link.url()).block();
                if (details != null) {
                    GithubCommitInfo[] last = Packer.githubCommitInfoFromJson(link.content());
                    if (last.length < details.length) {
                        log.debug("there have been commits made!");
                        linkUpdater.update(link.url().toString(), Packer.toJson(details));
                        shouldUpdate = true;
                    }
                }
            } else if (StackOverflowClient.isValidStackOverflowUri(link.url())) {
                StackOverflowAnswersResponse details
                    = stackOverflowClient.getQuestionAnswersByUri(link.url()).block();
                if (details != null) {
                    StackOverflowAnswersResponse last = Packer.stackOverflowAnswersFromJson(link.content());
                    if (last.items().length != details.items().length) {
                        log.debug("there have been answers made");
                        linkUpdater.update(link.url().toString(), Packer.toJson(details));
                        shouldUpdate = true;
                    }
                }
            }
            if (shouldUpdate) {
                List<Long> ids = linkService.listAllTgChatsWithLink(link.url());
                botClient.update(
                    new LinkUpdateRequest(
                        0, link.url(), "commits have been made", ids
                    )
                ).subscribe().dispose();
            }
        }
    }
}
