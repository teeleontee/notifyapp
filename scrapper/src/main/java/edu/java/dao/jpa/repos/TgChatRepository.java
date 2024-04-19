package edu.java.dao.jpa.repos;

import edu.java.dao.jpa.entities.TgChat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TgChatRepository extends JpaRepository<TgChat, Long> {

    Optional<TgChat> findById(Long chatId);
}
