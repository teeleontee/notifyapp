package edu.java.bot.configuration;

import edu.java.bot.client.retry.RetryPolicy;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.annotation.Validated;

@Validated
@ComponentScan
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @Value("${token}")
    String telegramToken,

    @Value("${name}")
    String telegramBotName,

    @Value("${scrapper-base-url}")
    String scrapperBaseUrl,

    @NotNull
    ClientRetry clientRetry
) {

    public record ClientRetry(
        @NotNull RetryPolicy retryPolicy,
        @NotNull Integer maxAttempts,
        @NotNull Integer delay,
        @NotNull List<Integer> supportedErrors
    ) {
    }
}
