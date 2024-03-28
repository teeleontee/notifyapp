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
    @Override
    public String toString() {
        return "GithubDetailsResponse{"
            + "owner=" + owner
            + ", name=" + name
            + ", id=" + id
            + '}';
    }

    public record Owner(
        @JsonProperty("login")
        String login
    ) {
        @Override public String toString() {
            return "Owner{"
                + "login=" + login
                + '}';
        }
    }
}
