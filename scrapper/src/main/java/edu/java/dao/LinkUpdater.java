package edu.java.dao;

import edu.java.dao.dto.LinkContent;
import java.util.List;

public interface LinkUpdater {
    void update(String link, String content);

    /**
     * Finds all links that need updating
     * @param time - in minutes what time frame we want to check for updates
     * @return Links that need to be updated, i.e. where checked time is bigger
     * than {@code time}
     */
    List<LinkContent> findAll(int time);
}
