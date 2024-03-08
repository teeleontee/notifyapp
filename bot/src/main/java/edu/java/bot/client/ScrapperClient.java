package edu.java.bot.client;

import edu.java.bot.client.dto.LinkResponse;
import edu.java.bot.client.dto.ListLinksResponse;
import reactor.core.publisher.Mono;

public interface ScrapperClient {
    Mono<Void> registerChat(long id);

    Mono<Void> deleteChat(long id);

    Mono<ListLinksResponse> getLinks(long id);

    Mono<LinkResponse> addLink(long id, String url);

    Mono<LinkResponse> deleteLink(long id, String url);
}
