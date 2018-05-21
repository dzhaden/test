package org.sandbox.model;

import java.util.List;

public class SearchResult{
    private final List<Long> result;
    private final long failedTasks;

    public SearchResult(List<Long> result, long failedTasks) {
        this.result = result;
        this.failedTasks = failedTasks;
    }

    public Long[] getResult() {
        Long[] array = new Long[result.size()];
        return result.toArray(array);
    }

    public long getFailedTasks() {
        return failedTasks;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "result=" + result +
                ", failedTasks=" + failedTasks +
                '}';
    }
}
