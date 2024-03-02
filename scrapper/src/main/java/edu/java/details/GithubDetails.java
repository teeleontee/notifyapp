package edu.java.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubDetails(
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
