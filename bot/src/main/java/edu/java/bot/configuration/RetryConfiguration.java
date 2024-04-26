package edu.java.bot.configuration;

import edu.java.bot.client.retry.LinearBackOffPolicy;
import edu.java.bot.client.retry.SupportedExceptionClassifierRetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
@Slf4j
public class RetryConfiguration {

    private static final long BACKOFF_COUNT = 1000L;

    @Bean
    public RetryTemplate retryTemplate(ApplicationConfig config) {
        RetryTemplate retryTemplate = new RetryTemplate();
        var cnf = config.clientRetry();

        log.debug("POLICY IS : {}", cnf.retryPolicy());
        switch (cnf.retryPolicy()) {
            case LINEAR -> {
                LinearBackOffPolicy linearBackOffPolicy = new LinearBackOffPolicy();
                linearBackOffPolicy.setIncrementer(cnf.delay());
                retryTemplate.setBackOffPolicy(linearBackOffPolicy);
            }
            case CONSTANT -> {
                FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                fixedBackOffPolicy.setBackOffPeriod(cnf.delay() * BACKOFF_COUNT);
                retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
            }
            case EXPONENTIAL -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(cnf.delay());
                retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
            }
            default -> {

            }
        }

        SupportedExceptionClassifierRetryPolicy retryPolicy
            = new SupportedExceptionClassifierRetryPolicy(cnf.supportedErrors(), cnf.maxAttempts());
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
