package org.sandbox.service;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.reflect.Whitebox;
import org.sandbox.model.SearchResult;
import org.sandbox.util.Task;
import org.sandbox.util.TokenListPreprocessor;
import org.sandbox.util.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.sandbox.TestUtil.toArray;

@RunWith(PowerMockRunner.class)
@SpringBootTest
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest(SearchService.class)
public class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @MockBean
    private Storage storage;
    @MockBean
    private TokenListPreprocessor preprocessor;

    @Test
    public void shouldReturnDifferentInstances() throws Exception {
        Task task1 = Whitebox.invokeMethod(searchService, "newTask", 1l, "1", Lists.newArrayList());
        Task task2 = Whitebox.invokeMethod(searchService, "newTask", 1l, "1", Lists.newArrayList());
        Assert.assertNotEquals(task1, task2);
    }

    @Test
    public void shouldSplit() throws Exception {
        String tokens = "abc,hft, jht, juuuy, sdrr fggf t,,";
        String[] expected = {"abc", "hft", " jht", " juuuy", " sdrr fggf t"};
        List<String> split = Whitebox.invokeMethod(searchService, "split", tokens);
        Assert.assertArrayEquals(expected, toArray(split));
    }

    @Test
    public void shouldConvertFutureListToSearchResult() throws Exception {
        FutureTask<Long> future1 = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return -1l;
            }
        });
        FutureTask<Long> future2 = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                throw new Exception("Test exception");
            }
        });
        FutureTask<Long> future3 = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return 1l;
            }
        });
        future1.run();
        future2.run();
        future3.run();

        List<Future<Long>> futures = new ArrayList<>();
        futures.add(future1);
        futures.add(future2);
        futures.add(future3);

        SearchResult actual = Whitebox.invokeMethod(searchService, "futureListToSearchResult", futures);

        Arrays.sort(actual.getResult());

        Long[] expectedResult = {1l};

        Assert.assertArrayEquals(expectedResult, actual.getResult());
        Assert.assertEquals(1l, actual.getFailedTasks());
    }

    @Test
    public void shouldPrepareListOfTasks() throws Exception {
        SearchService searchServiceSpy = spy(searchService);

        List<String> tokens = new ArrayList<>();
        tokens.add("token1");

        Map<Long, String> map = new HashMap<>();
        map.put(1l, "content1");
        map.put(2l, "content2");

        List<Task> tasks = Whitebox.invokeMethod(searchServiceSpy, "prepareTaskList", tokens, map);

        verifyPrivate(searchServiceSpy, times(1)).invoke("newTask", 1l, "content1", tokens);
        verifyPrivate(searchServiceSpy, times(1)).invoke("newTask", 2l, "content2", tokens);

        Assert.assertEquals(2, tasks.size());
    }

    @Test
    public void shouldSearchForTokens() throws Exception {
        SearchService searchServiceSpy = spy(searchService);

        Map<Long, String> map = new HashMap<>();
        map.put(1l, "iregqigi uiegvreq token2 ehurei,token3rufhi urgfiqg token1");
        map.put(2l, "wfegjkbne,wuhgurg qugheetoken3,grewgbiqrwnb token1");
        map.put(3l, "fiegeiww token1 igeignewitoken2reuig reugehgwe token3");

        given(storage.retrieveAll()).willReturn(map);

        String tokens = "token1,token2,token3";

        SearchResult actual = searchServiceSpy.searchFor(tokens);

        verifyPrivate(searchServiceSpy, times(1)).invoke("split", tokens);
        verify(preprocessor, times(1)).deleteSubtokens(anyList());
        verifyPrivate(searchServiceSpy, times(1)).invoke("prepareTaskList", anyList(), eq(map));
        verifyPrivate(searchServiceSpy, times(1)).invoke("futureListToSearchResult", anyList());

        Long[] expectedResult = {1l, 3l};
        long expectedFailedTasks = 0l;

        Arrays.sort(actual.getResult());

        Assert.assertArrayEquals(expectedResult, actual.getResult());
        Assert.assertEquals(expectedFailedTasks, actual.getFailedTasks());
    }

}
