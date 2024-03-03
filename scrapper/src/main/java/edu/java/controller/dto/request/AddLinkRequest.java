package edu.java.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(@NotNull @NotBlank URI link) {
}
