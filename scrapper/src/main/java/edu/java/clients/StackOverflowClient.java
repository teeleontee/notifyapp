package edu.java.clients;

import edu.java.clients.details.StackOverflowAnswersResponse;
import edu.java.clients.details.StackOverflowDetailsResponse;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<StackOverflowDetailsResponse> getQuestionInfo(String id);

    Mono<StackOverflowDetailsResponse> getQuestionInfoByUri(URI url);

    Mono<StackOverflowAnswersResponse> getQuestionAnswers(String id);

    Mono<StackOverflowAnswersResponse> getQuestionAnswersByUri(URI url);

    static boolean isValidStackOverflowUri(URI url) {
        Pattern pattern = Pattern.compile("https://stackoverflow.com/.*/.*/.*");
        Matcher matcher = pattern.matcher(url.toString());
        return matcher.find();
    }
}
