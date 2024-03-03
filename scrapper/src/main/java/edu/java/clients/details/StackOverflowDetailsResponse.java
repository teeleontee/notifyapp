package edu.java.clients.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StackOverflowDetailsResponse(
    @JsonProperty("items")
    List<Item> details

) {
    public record Item(
        @JsonProperty("title")
        String title,
        @JsonProperty("score")
        int score,
        @JsonProperty("question_id")
        int questionId
    ) {
    }
}
