package edu.java.dao.jpa;

import edu.java.dao.TgChatService;
import edu.java.dao.jpa.entities.TgChat;
import edu.java.dao.jpa.repos.TgChatRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaTgChatService implements TgChatService {

    private final TgChatRepository tgChatRepository;

    public JpaTgChatService(TgChatRepository tgChatRepository) {
        this.tgChatRepository = tgChatRepository;
    }

    @Override
    public void register(long chatId) {
        TgChat tgChat = new TgChat(chatId);
        tgChatRepository.save(tgChat);
    }

    @Override
    public void unregister(long chatId) {
        Optional<TgChat> tgChat = tgChatRepository.findById(chatId);
        tgChat.ifPresent(tgChatRepository::delete);
    }
}
