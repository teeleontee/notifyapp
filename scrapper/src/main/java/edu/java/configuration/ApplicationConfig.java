package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;

@Validated
@ComponentScan
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,

    @NotNull
    @Value("${github-base-url}")
    String githubBaseUrl,

    @NotNull
    @Value("${stack-overflow-base-url}")
    String stackOverflowBaseUrl
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

}
