package edu.java.clients;

import edu.java.clients.details.StackOverflowDetailsResponse;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class StackOverflowClientImpl implements StackOverflowClient {
    private final WebClient stackoverflowClient;

    public StackOverflowClientImpl(@Qualifier("stackOverflowWebClient") WebClient stackoverflowClient) {
        this.stackoverflowClient = stackoverflowClient;
    }

    @Override
    public Mono<StackOverflowDetailsResponse> getQuestionInfo(String id) {
        return stackoverflowClient.get()
            .uri("/questions/{id}?site=stackoverflow", id)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new ApiException("StackOverflow error")))
            .bodyToMono(StackOverflowDetailsResponse.class);
    }
}
