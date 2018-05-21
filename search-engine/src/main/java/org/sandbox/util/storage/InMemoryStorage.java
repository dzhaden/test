package org.sandbox.util.storage;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryStorage implements Storage {
    private final Map<Long, String> storage = new ConcurrentHashMap<>();
    private long sequence;

    @Override
    public synchronized long insertDocument(String content) {
        sequence++;
        storage.put(sequence, content);
        return sequence;
    }

    @Override
    public String retrieveDocument(long id) {
        return storage.get(id);
    }

    @Override
    public Map<Long, String> retrieveAll() {
        return Collections.unmodifiableMap(storage);
    }
}
