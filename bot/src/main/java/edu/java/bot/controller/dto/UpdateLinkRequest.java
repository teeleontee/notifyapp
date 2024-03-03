package edu.java.bot.controller.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record UpdateLinkRequest(
    @NotNull
    long id,
    @NotNull
    URI uri,
    @NotNull
    String description,
    @NotNull
    List<Long> tgChatIds
) {
}
