package edu.java.configuration;

import edu.java.dao.jpa.JpaLinkService;
import edu.java.dao.jpa.JpaLinkUpdater;
import edu.java.dao.jpa.JpaTgChatService;
import edu.java.dao.jpa.repos.LinkRepository;
import edu.java.dao.jpa.repos.TaskRepository;
import edu.java.dao.jpa.repos.TgChatRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "connection-type", havingValue = "jpa")
public class JpaConfig {

    private final LinkRepository linkRepository;
    private final TaskRepository taskRepository;
    private final TgChatRepository tgChatRepository;

    public JpaConfig(
        LinkRepository linkRepository,
        TaskRepository taskRepository,
        TgChatRepository tgChatRepository
    ) {
        this.linkRepository = linkRepository;
        this.taskRepository = taskRepository;
        this.tgChatRepository = tgChatRepository;
    }

    @Bean
    public JpaLinkService jpaLinkService() {
        return new JpaLinkService(linkRepository, taskRepository);
    }

    @Bean
    public JpaLinkUpdater jpaLinkUpdater() {
        return new JpaLinkUpdater(linkRepository);
    }

    @Bean
    public JpaTgChatService jpaTgChatService() {
        return new JpaTgChatService(tgChatRepository);
    }
}
