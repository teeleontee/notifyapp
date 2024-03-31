package edu.java.dao.jpa.repos;

import edu.java.dao.jpa.entities.Task;
import edu.java.dao.jpa.entities.TaskPk;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, TaskPk> {
    Optional<Task> findAllByChatIdAndLinkId(Long chatId, Long linkId);

    List<Task> findAllByChatId(Long chatId);

    List<Task> findAllByLinkId(Long linkId);
}
