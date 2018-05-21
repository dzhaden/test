package org.sandbox.service;

import org.sandbox.util.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    @Autowired
    private Storage storage;

    public String getDocument(long id) {
        return storage.retrieveDocument(id);
    }

    public long saveDocument(String content) {
        return storage.insertDocument(content);
    }
}
