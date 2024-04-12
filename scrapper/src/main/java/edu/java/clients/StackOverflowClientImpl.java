package edu.java.clients;

import edu.java.clients.details.StackOverflowDetailsResponse;
import java.net.URI;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private final WebClient stackoverflowClient;

    private final RetryTemplate retryTemplate;

    public StackOverflowClientImpl(@Qualifier("stackOverflowWebClient") WebClient stackoverflowClient, RetryTemplate retryTemplate) {
        this.stackoverflowClient = stackoverflowClient;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public Mono<StackOverflowDetailsResponse> getQuestionInfo(String id) {
        return retryTemplate.execute(ctx -> stackoverflowClient.get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException("StackOverflow error"))
            )
            .bodyToMono(StackOverflowDetailsResponse.class));
    }

    @Override
    public Mono<StackOverflowDetailsResponse> getQuestionInfoByUri(URI url) {
        return getQuestionInfo(stackOverflowQuestionIdFromUri(url));
    }

    private String stackOverflowQuestionIdFromUri(URI url) {
        return url.getPath().split("/")[2];
    }
}
