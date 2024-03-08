package edu.java.clients;

import edu.java.clients.details.GithubDetailsResponse;
import reactor.core.publisher.Mono;

public interface GithubClient {
    Mono<GithubDetailsResponse> getGithubInfo(String username, String repo);
}
