package edu.java.dao.jpa;

import edu.java.dao.LinkService;
import edu.java.dao.jpa.entities.Link;
import edu.java.dao.jpa.entities.Task;
import edu.java.dao.jpa.repos.LinkRepository;
import edu.java.dao.jpa.repos.TaskRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class JpaLinkService implements LinkService {

    private final LinkRepository linkRepository;

    private final TaskRepository taskRepository;

    public JpaLinkService(LinkRepository linkRepository, TaskRepository taskRepository) {
        this.linkRepository = linkRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public void add(long tgChatId, URI url) {
        Link link = new Link();
        link.setContent(null);
        link.setUrl(url.toString());
        linkRepository.save(link);
        var task = new Task();
        task.setLinkId(link.getId());
        task.setChatId(tgChatId);
        taskRepository.save(task);
    }

    @Override
    public void remove(long tgChatId, URI url) {
        Long linkId = linkRepository.findByUrl(url.toString()).get().getId();
        Optional<Task> task = taskRepository.findAllByChatIdAndLinkId(tgChatId, linkId);
        task.ifPresent(taskRepository::delete);
    }

    @Override
    @Transactional
    public List<edu.java.dao.dto.Link> listAll(long tgChatId) {
        return taskRepository.findAllByChatId(tgChatId)
            .stream()
            .map(it -> linkRepository.findById(it.getLinkId()).get())
            .map(it -> new edu.java.dao.dto.Link(URI.create(it.getUrl())))
            .toList();
    }

    @Override
    @Transactional
    public List<Long> listAllTgChatsWithLink(URI url) {
        Long id = linkRepository.findByUrl(url.toString()).get().getId();
        return taskRepository.findAllByLinkId(id)
            .stream()
            .map(Task::getChatId)
            .toList();
    }
}
