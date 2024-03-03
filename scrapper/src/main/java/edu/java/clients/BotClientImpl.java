package edu.java.clients;

import edu.java.clients.details.LinkUpdateRequest;
import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.exceptions.ApiErrorResponse;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BotClientImpl implements BotClient {

    private final WebClient botWebClient;

    public BotClientImpl(@Qualifier("botWebClient") WebClient botWebClient) {
        this.botWebClient = botWebClient;
    }

    @Override
    public Mono<Void> update(LinkUpdateRequest request) {
        return botWebClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
                    .map(response -> new ApiException(response.exceptionMessage()))
            )
            .bodyToMono(Void.class);
    }
}
