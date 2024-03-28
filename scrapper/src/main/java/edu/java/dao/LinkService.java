package edu.java.dao;

import edu.java.dao.dto.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId);

    List<Long> listAllTgChatsWithLink(URI url);
}
