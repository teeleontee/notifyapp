package edu.java.configuration;

import edu.java.clients.retry.RetryPolicy;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
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
    ClientRetry clientRetry,

    @NotNull
    String githubBaseUrl,

    @NotNull
    String stackOverflowBaseUrl,

    @NotNull
    String botBaseUrl,

    @NotNull
    AccessType connectionType
) {
    public record Scheduler(boolean enable,
                            @NotNull Duration interval,
                            @NotNull Duration forceCheckDelay) {
    }

    public record ClientRetry(
        @NotNull RetryPolicy retryPolicy,
        @NotNull Integer maxAttempts,
        @NotNull Integer delay,
        @NotNull List<Integer> supportedErrors
    ) {
    }
}
