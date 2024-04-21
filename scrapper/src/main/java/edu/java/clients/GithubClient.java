package edu.java.clients;

import edu.java.clients.details.GithubCommitInfo;
import edu.java.clients.details.GithubDetailsResponse;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import reactor.core.publisher.Mono;

public interface GithubClient {
    Mono<GithubDetailsResponse> getGithubRepoInfo(String username, String repo);

    Mono<GithubDetailsResponse> getGithubInfoByUri(URI url);

    Mono<GithubCommitInfo[]> getGithubRepoCommitInfo(String username, String repo);

    Mono<GithubCommitInfo[]> getGithubRepoCommitInfoByUri(URI url);

    static boolean isValidGithubUrl(URI url) {
        Pattern pattern = Pattern.compile("https://github.com/.*/.*");
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.find();
    }
}
