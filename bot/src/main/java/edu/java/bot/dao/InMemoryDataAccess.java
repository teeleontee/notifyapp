package edu.java.bot.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDataAccess implements DataAccess {
    private final Map<Long, List<String>> db = new HashMap<>();

    @Override
    public List<String> getList(long id) {
        if (db.containsKey(id)) {
            return db.get(id);
        }
        return null;
    }

    @Override
    public boolean addLink(long id, Object url) {
        if (db.containsKey(id)) {
            return db.get(id).add(url.toString());
        }
        List<String> urls = new ArrayList<>();
        db.put(id, urls);
        return db.get(id).add(url.toString());
    }

    @Override
    public boolean removeLink(long id, Object url) {
        if (db.containsKey(id)) {
            return db.get(id).remove(url.toString());
        }
        return false;
    }
}
