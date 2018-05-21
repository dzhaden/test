package org.sandbox.service;

import org.sandbox.model.SearchResult;
import org.sandbox.util.Task;
import org.sandbox.util.TokenListPreprocessor;
import org.sandbox.util.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class SearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Storage storage;
    @Autowired
    private TokenListPreprocessor preprocessor;

    @Autowired
    private BeanFactory beanFactory;

    @Value("${search.engine.token.separator}")
    private String tokenSeparator;
    @Value("${search.engine.concurrency.level}")
    private Integer concurrencyLevel;

    public SearchResult searchFor(String tokens) {
        List<String> tokenList = split(tokens);

        preprocessor.deleteSubtokens(tokenList);

        List<Task> tasks = prepareTaskList(tokenList, storage.retrieveAll());

        ExecutorService executorService = Executors.newFixedThreadPool(concurrencyLevel);
        List<Future<Long>> futures = null;
        try {
            futures = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            logger.trace("Search process was interrupted", e);
        }
        executorService.shutdown();

        return futureListToSearchResult(futures);
    }

    private List<String> split(String tokens) {
        return Arrays.asList(tokens.split(tokenSeparator));
    }

    private List<Task> prepareTaskList(List<String> tokens, Map<Long, String> contentMap) {
        List<Task> tasks = new ArrayList<>();
        for (Map.Entry<Long, String> entry : contentMap.entrySet()) {
            tasks.add(newTask(entry.getKey(), entry.getValue(), tokens));

        }
        return tasks;
    }

    private Task newTask(long id, String content, List<String> tokens) {
        return beanFactory.getBean(Task.class, id, content, tokens);
    }

    private SearchResult futureListToSearchResult(List<Future<Long>> futures) {
        List<Long> resultList = new ArrayList<>();
        long failedTaskCount = 0;
        for (Future<Long> future : futures) {
            Long id = null;
            try {
                id = future.get();
            } catch (InterruptedException e) {
                logger.trace("Search process was interrupted", e);
            } catch (ExecutionException e) {
                logger.debug("Task was failed", e);
                failedTaskCount++;
                continue;
            }
            if (id != -1) {
                resultList.add(id);
            }
        }
        return new SearchResult(resultList, failedTaskCount);
    }
}
