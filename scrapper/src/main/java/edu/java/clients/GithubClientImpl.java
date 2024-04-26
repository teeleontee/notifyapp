package edu.java.clients;

import edu.java.clients.details.GithubCommitInfo;
import edu.java.clients.details.GithubDetailsResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GithubClientImpl implements GithubClient {
    private final WebClient githubWebClient;

    private final RetryTemplate retryTemplate;

    private static final String GH_ERR = "Github error";

    @Override
    public Mono<GithubDetailsResponse> getGithubRepoInfo(String username, String repo) {
        return retryTemplate.execute(ctx -> githubWebClient.get()
            .uri("/repos/{username}/{repo}", username, repo)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException(GH_ERR))
            )
            .bodyToMono(GithubDetailsResponse.class));
    }

    @Override
    public Mono<GithubDetailsResponse> getGithubInfoByUri(URI url) {
        return getGithubRepoInfo(
            githubUsernameFromUri(url),
            githubRepoFromUri(url)
        );
    }

    @Override
    public Mono<GithubCommitInfo[]> getGithubRepoCommitInfo(String username, String repo) {
        return retryTemplate.execute(ctx -> githubWebClient.get()
            .uri("/repos/{username}/{repo}/commits", username, repo)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException(GH_ERR))
            )
            .bodyToMono(GithubCommitInfo[].class));
    }

    @Override
    public Mono<GithubCommitInfo[]> getGithubRepoCommitInfoByUri(URI url) {
        return getGithubRepoCommitInfo(
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
