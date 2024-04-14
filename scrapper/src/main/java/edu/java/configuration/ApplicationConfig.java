package edu.java.configuration;

import edu.java.clients.retry.RetryPolicy;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
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
    ClientRetry clientRetry,

    @NotNull
    @Value("${app.github-base-url:https://api.github.com}")
    String githubBaseUrl,

    @NotNull
    @Value("${app.stack-overflow-base-url:https://api.stackexchange.com/2.3}")
    String stackOverflowBaseUrl,

    @NotNull
    @Value("${app.bot-base-url:https://localhost:8090}")
    String botBaseUrl,

    @NotNull
    @Value("${app.connection-type:jdbc}")
    AccessType connectionType

//    @NotNull
//    @Value(value = "${app.retry.policy}")
//    RetryPolicy retryPolicy,
//
//    @Value("${app.retry.attempts}")
//    Integer maxAttempts,
//
//    @Value("${app.retry.delay}")
//    Integer delay,
//
//    @Value("${app.retry.supported-errors}")
//    List<Integer> supportedErrors
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
