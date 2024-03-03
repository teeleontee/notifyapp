package edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    @Bean
    public WebClient scrapperWebClient(@Qualifier("applicationConfig") ApplicationConfig config) {
        return WebClient.builder()
            .baseUrl(config.scrapperBaseUrl)
            .build();
    }
}
