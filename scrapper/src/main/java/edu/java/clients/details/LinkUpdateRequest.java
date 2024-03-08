package edu.java.clients.details;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    @NotNull
    long id,
    @NotNull
    URI url,
    @NotNull
    String description,
    @NotNull
    List<Long> tgChatIds
) {
}
