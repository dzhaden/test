package org.sandbox.util.storage;

import java.util.Map;

public interface Storage {
    long insertDocument(String content);
    String retrieveDocument(long id);
    Map<Long, String> retrieveAll();
}
