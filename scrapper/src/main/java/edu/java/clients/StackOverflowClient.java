package edu.java.clients;

import edu.java.clients.details.StackOverflowDetailsResponse;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<StackOverflowDetailsResponse> getQuestionInfo(String id);
}
