package edu.java.bot.dao;

import java.util.List;

public interface DataAccess {
    List<?> getList(long id);

    boolean addLink(long id, Object url);

    boolean removeLink(long id, Object url);
}
