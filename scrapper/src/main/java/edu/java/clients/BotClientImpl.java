package edu.java.clients;

import edu.java.clients.details.LinkUpdateRequest;
import edu.java.exceptions.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BotClientImpl implements BotClient {

    private final WebClient botWebClient;

    private final RetryTemplate retryTemplate;

    @Override
    public Mono<Void> update(LinkUpdateRequest request) {
        return retryTemplate.execute(ctx -> botWebClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(response -> new ApiException(response.exceptionMessage()))
            )
            .bodyToMono(Void.class));
    }
}
