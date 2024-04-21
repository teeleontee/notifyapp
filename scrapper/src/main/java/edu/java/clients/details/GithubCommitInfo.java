package edu.java.clients.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubCommitInfo(
    @JsonProperty("sha")
    String sha,

    @JsonProperty("commit")
    Commit commit
) {
    @Override public String toString() {
        return "GithubCommitInfo{"
            + "sha='" + sha + '\''
            + ", commit=" + commit
            + '}';
    }

    public record Commit(
        @JsonProperty("author")
        Author author,

        @JsonProperty("message")
        String message
    ) {
        @Override
        public String toString() {
            return "Commit{"
                + "author='" + author + '\''
                + ", message='" + message + '\''
                + '}';
        }

        public record Author(
            @JsonProperty("name")
            String name,
            @JsonProperty("date")
            String date) {
            @Override
            public String toString() {
                return "Author{"
                    + "name='" + name + '\''
                    + ", date='" + date + '\''
                    + '}';
            }
        }

    }
}
