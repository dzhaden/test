package org.sandbox.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sandbox.util.search.SearchEngine;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    @MockBean
    private SearchEngine searchEngine;

    @Autowired
    private BeanFactory beanFactory;

    @Test
    public void shouldFindAllTokens() throws Exception {
        String content = "content";

        List<String> tokens = new ArrayList<>();
        tokens.add("token1");
        tokens.add("token2");
        tokens.add("token3");

        Long expected = 1l;

        given(searchEngine.searchFor(anyString(), anyString())).willReturn(true);

        Task task = beanFactory.getBean(Task.class, expected, content, tokens);
        Long actual = task.call();

        verify(searchEngine, times(3)).searchFor(anyString(), anyString());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldNotFindAllTokens() throws Exception {
        String content = "content";

        List<String> tokens = new ArrayList<>();
        tokens.add("token1");
        tokens.add("token2");
        tokens.add("token3");

        Long id = 1l;

        given(searchEngine.searchFor(eq("token1"), anyString())).willReturn(true);
        given(searchEngine.searchFor(eq("token2"), anyString())).willReturn(false);
        given(searchEngine.searchFor(eq("token3"), anyString())).willReturn(true);

        Task task = beanFactory.getBean(Task.class, id, content, tokens);
        Long actual = task.call();

        verify(searchEngine, times(2)).searchFor(anyString(), anyString());

        Assert.assertEquals(new Long(-1), actual);
    }
}
