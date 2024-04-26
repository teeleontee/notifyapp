package edu.java.clients.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowAnswersResponse(
    @JsonProperty("has_more")
    Boolean hasMore,

    @JsonProperty("quota_max")
    Integer quotaMax,

    @JsonProperty("quota_remaining")
    Integer quotaRemaining,

    @JsonProperty("items")
    AnswerItem[] items
) {
    public record AnswerItem(
        @JsonProperty("score")
        Integer score,

        @JsonProperty("answer_id")
        Long answerId,

        @JsonProperty("body")
        String body,

        @JsonProperty("owner")
        Owner owner
    ) {
    }

    public record Owner(
        @JsonProperty("account_id")
        Long accountId,

        @JsonProperty("reputation")
        Long reputation
    ) {

    }
}
