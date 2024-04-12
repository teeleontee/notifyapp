package edu.java.clients.retry;

import org.springframework.classify.Classifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;

public class SupportedExceptionClassifierRetryPolicy extends
    ExceptionClassifierRetryPolicy {

    public SupportedExceptionClassifierRetryPolicy(List<Integer> supportedCodes, Integer maxAttempts)
    {

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(maxAttempts);

        this.setExceptionClassifier((Classifier<Throwable, RetryPolicy>) classifiable -> {
            // Do not retry for other exceptions
            if (!(classifiable instanceof WebClientResponseException))
            {
                return new NeverRetryPolicy();
            }

            // Retry only when supported WebClientResponseException was thrown
            if (supportedCodes.contains(((WebClientResponseException)classifiable).getStatusCode().value())) {
                return simpleRetryPolicy;
            }

            return new NeverRetryPolicy();
        });
    }
}
