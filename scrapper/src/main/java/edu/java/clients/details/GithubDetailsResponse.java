package edu.java.clients.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubDetailsResponse(
    @JsonProperty("owner")
    Owner owner,

    @JsonProperty("name")
    String name,

    @JsonProperty("id")
    int id

    ) {
    public record Owner(
        @JsonProperty("login")
        String login
    ) {
    }
}
