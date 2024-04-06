package edu.java.clients;

import edu.java.clients.details.GithubDetailsResponse;
import java.net.URI;
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
    public Mono<GithubDetailsResponse> getGithubInfo(String username, String repo) {
        return githubWebClient.get()
            .uri("/repos/{username}/{repo}", username, repo)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException("Github error"))
            )
            .bodyToMono(GithubDetailsResponse.class);
    }

    @Override
    public Mono<GithubDetailsResponse> getGithubInfoByUri(URI url) {
        return getGithubInfo(
            githubUsernameFromUri(url),
            githubRepoFromUri(url)
        );
    }

    private String githubUsernameFromUri(URI url) {
        return url.getPath().split("/")[1];
    }

    private String githubRepoFromUri(URI url) {
        return url.getPath().split("/")[2];
    }
}
