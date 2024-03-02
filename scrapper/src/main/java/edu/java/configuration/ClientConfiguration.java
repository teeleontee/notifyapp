package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public WebClient githubWebClient(ApplicationConfig config) {
        return WebClient.builder()
            .baseUrl(config.githubBaseUrl())
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient(ApplicationConfig config) {
        return WebClient.builder()
            .baseUrl(config.stackOverflowBaseUrl())
            .build();
    }
}
