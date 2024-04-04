package edu.java.configuration;

import edu.java.dao.jpa.JpaLinkService;
import edu.java.dao.jpa.JpaLinkUpdater;
import edu.java.dao.jpa.JpaTgChatService;
import edu.java.dao.jpa.repos.LinkRepository;
import edu.java.dao.jpa.repos.TaskRepository;
import edu.java.dao.jpa.repos.TgChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "connection-type", havingValue = "jpa")
public class JpaConfig {

    @Bean
    public JpaLinkService jpaLinkService(LinkRepository linkRepository, TaskRepository taskRepository) {
        return new JpaLinkService(linkRepository, taskRepository);
    }

    @Bean
    public JpaLinkUpdater jpaLinkUpdater(LinkRepository linkRepository) {
        return new JpaLinkUpdater(linkRepository);
    }

    @Bean
    public JpaTgChatService jpaTgChatService(TgChatRepository tgChatRepository) {
        return new JpaTgChatService(tgChatRepository);
    }
}
