package edu.java.configuration;

import edu.java.clients.retry.LinearBackOffPolicy;
import edu.java.clients.retry.SupportedExceptionClassifierRetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import java.util.List;

@EnableRetry
@Configuration
@Slf4j
public class RetryConfiguration {

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
                fixedBackOffPolicy.setBackOffPeriod(cnf.delay() * 1000L);
                retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
            }
            case EXPONENTIAL -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(cnf.delay());
                retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);
            }
        }

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();

        SupportedExceptionClassifierRetryPolicy retryPolicy
            = new SupportedExceptionClassifierRetryPolicy(List.of(1, 2, 3), cnf.maxAttempts());
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }
}
