package edu.java.configuration;

import edu.java.clients.retry.LinearBackOffPolicy;
import edu.java.clients.retry.SupportedExceptionClassifierRetryPolicy;
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

    private final long backoffCount = 1000L;

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
                fixedBackOffPolicy.setBackOffPeriod(cnf.delay() * backoffCount);
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
