package edu.java.clients;

import edu.java.details.GithubDetails;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GithubClientImpl implements GithubClient {
    private final WebClient githubWebClient;

    public GithubClientImpl(@Qualifier("githubWebClient") WebClient githubWebClient) {
        this.githubWebClient = githubWebClient;
    }

    @Override
    public Mono<GithubDetails> getGithubInfo(String username, String repo) {
        return githubWebClient.get()
            .uri("/repos/{username}/{repo}", username, repo)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException("Github error")))
            .bodyToMono(GithubDetails.class);
    }
}
