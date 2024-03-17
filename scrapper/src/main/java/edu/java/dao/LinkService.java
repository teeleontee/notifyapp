package edu.java.dao;

import edu.java.dao.dto.Link;
import java.net.URI;
import java.util.List;

public interface LinkService {
    boolean add(long tgChatId, URI url);

    boolean remove(URI url);

    List<Link> listAll(long tgChatId);
}
