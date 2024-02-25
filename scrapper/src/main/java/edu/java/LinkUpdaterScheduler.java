package edu.java;

import edu.java.clients.GithubClient;
import edu.java.clients.StackOverflowClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class LinkUpdaterScheduler {

    public final GithubClient githubClient;
    public final StackOverflowClient stackOverflowClient;

    public LinkUpdaterScheduler(GithubClient client, StackOverflowClient stackOverflowClient) {
        this.githubClient = client;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    void update() {
//        githubClient.getGithubInfo("teeleontee", "notifyapp")
//            .subscribe(
//                value -> System.err.printf("name: %s, id: %s%n", value.name(), value.id()),
//                Throwable::printStackTrace
//            );
//        stackOverflowClient.getQuestionInfo("75734651")
//            .subscribe(
//                value -> {
//                    var details = value.getDetails();
//                    if (!details.isEmpty()) {
//                        var first = details.getFirst();
//                        System.err.printf("question: %s, questionId: %s, score: %s",
//                            first.title, first.questionId, first.score
//                        );
//                    }
//                },
//                Throwable::printStackTrace
//            );
        log.debug("scheduled");
    }
}
