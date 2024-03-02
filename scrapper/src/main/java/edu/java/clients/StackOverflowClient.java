package edu.java.clients;

import edu.java.details.StackOverflowDetails;
import reactor.core.publisher.Mono;

public interface StackOverflowClient {
    Mono<StackOverflowDetails> getQuestionInfo(String id);
}
