package io.vepo.bookstore.infra;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 * This is my Database in memory. Don't bother me, this is for samples
 * applications. I will not use a database.
 * 
 * @author Victor Osório <victor.perticarrari@gmail.com>
 *
 */
@ApplicationScoped
public class MemoryDatabase {

    private Map<Class<?>, Map<String, Object>> database;

    @PostConstruct
    void setup() {
        database = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> find(String key, Class<T> cls) {
        Map<String, Object> table = database.computeIfAbsent(cls, __ -> new HashMap<>());
        return (Optional<T>) Optional.ofNullable(table.getOrDefault(key, null));
    }

    public <T> void insert(String key, T value) {
        Map<String, Object> table = database.computeIfAbsent(value.getClass(), __ -> new HashMap<>());
        table.put(key, value);
    }

}
