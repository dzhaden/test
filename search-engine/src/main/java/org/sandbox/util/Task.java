package org.sandbox.util;

import org.sandbox.util.search.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Lazy
public class Task implements Callable<Long> {
    @Autowired
    private SearchEngine searchEngine;

    private final long id;
    private final String content;
    private final List<String> tokens;

    public Task(long id, String content, List<String> tokens) {
        this.id = id;
        this.content = content;
        this.tokens = tokens;
    }

    @Override
    public Long call() throws Exception {
        for (String token : tokens) {
            if (! searchEngine.searchFor(token, content)) {
                return -1l;
            }
        }

        return id;
    }
}
