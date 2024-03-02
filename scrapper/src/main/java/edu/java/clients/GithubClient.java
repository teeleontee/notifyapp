package edu.java.clients;

import edu.java.details.GithubDetails;
import reactor.core.publisher.Mono;

public interface GithubClient {
    Mono<GithubDetails> getGithubInfo(String username, String repo);
}
