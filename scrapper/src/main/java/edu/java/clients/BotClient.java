package edu.java.clients;

import edu.java.clients.details.LinkUpdateRequest;
import reactor.core.publisher.Mono;

public interface BotClient {
    Mono<Void> update(LinkUpdateRequest request);
}
