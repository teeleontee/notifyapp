package edu.java.bot.client;

import edu.java.bot.client.dto.AddLinkRequest;
import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import edu.java.bot.client.dto.RemoveLinkRequest;
import edu.java.bot.exceptions.ApiErrorResponse;
import java.net.URI;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScrapperClientImpl implements ScrapperClient {

    private static final String CHAT_HEADER = "Tg-Chat-Id";
    private static final String LINKS_EP = "/links";
    private static final String CHATS_EP = "/tg-chat/{id}";

    private final WebClient scrapperWebCLient;

    private final RetryTemplate retryTemplate;

    @Override
    public Mono<Void> registerChat(long id) {
        return retryTemplate.execute(ctx -> scrapperWebCLient.post()
            .uri(CHATS_EP, id)
            .retrieve()
            .onStatus(isErrorStatus(), handleClientEror())
            .bodyToMono(Void.class));
    }

    @Override
    public Mono<Void> deleteChat(long id) {
        return retryTemplate.execute(ctx -> scrapperWebCLient.delete()
            .uri(CHATS_EP, id)
            .retrieve()
            .onStatus(isErrorStatus(), handleClientEror())
            .bodyToMono(Void.class));
    }

    @Override
    public Mono<ListLinksResponse> getLinks(long id) {
        return retryTemplate.execute(ctx -> scrapperWebCLient.get()
            .uri(LINKS_EP)
            .header(CHAT_HEADER, Long.toString(id))
            .retrieve()
            .onStatus(isErrorStatus(), handleClientEror())
            .bodyToMono(ListLinksResponse.class));
    }

    @Override
    public Mono<LinkResponse> addLink(long id, String url) {
        return retryTemplate.execute(ctx -> scrapperWebCLient.post()
            .uri(LINKS_EP)
            .header(CHAT_HEADER, Long.toString(id))
            .bodyValue(new AddLinkRequest(URI.create(url)))
            .retrieve()
            .onStatus(isErrorStatus(), handleClientEror())
            .bodyToMono(LinkResponse.class));
    }

    @Override
    public Mono<LinkResponse> deleteLink(long id, String url) {
        return retryTemplate.execute(ctx -> scrapperWebCLient.post()
            .uri(LINKS_EP)
            .header(CHAT_HEADER, Long.toString(id))
            .bodyValue(new RemoveLinkRequest(URI.create(url)))
            .retrieve()
            .onStatus(isErrorStatus(), handleClientEror())
            .bodyToMono(LinkResponse.class));
    }

    @NotNull private static Predicate<HttpStatusCode> isErrorStatus() {
        return httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError();
    }

    @NotNull private static Function<ClientResponse, Mono<? extends Throwable>> handleClientEror() {
        return clientResponse -> clientResponse.bodyToMono(ApiErrorResponse.class)
            .map(response -> new ApiException(response.exceptionMessage()));
    }
}
