package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    String githubBaseUrl,
    @NotNull
    String stackOverflowBaseUrl
) {
    public ApplicationConfig {
        githubBaseUrl = "https://api.github.com";
        stackOverflowBaseUrl = "https://api.stackexchange.com/2.3";
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

}
