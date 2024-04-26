package edu.java.scrapper.service;

import edu.java.dao.LinkService;
import edu.java.dao.TgChatService;
import edu.java.dao.jpa.JpaLinkService;
import edu.java.dao.jpa.JpaTgChatService;
import edu.java.dao.jpa.repos.LinkRepository;
import edu.java.dao.jpa.repos.TaskRepository;
import edu.java.dao.jpa.repos.TgChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.URI;
import java.util.List;

@SpringBootTest
public class JpaServiceTest extends ServiceTest {

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TgChatRepository tgChatRepository;

    @Override
    protected TgChatService getTgChatService() {
        return new JpaTgChatService(tgChatRepository);
    }

    @Override
    protected LinkService getLinkService() {
        return new JpaLinkService(linkRepository, taskRepository);
    }

    @Override
    protected Long getProfile() {
        return 9876543L;
    }

    @Override
    protected List<URI> getTestCases() {
        return List.of(
            URI.create("https://github.com/HardhatChad/ore"),
            URI.create("https://github.com/intel-analytics/ipex-llm")
        );
    }
}
