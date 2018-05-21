package org.sandbox.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sandbox.model.SearchResult;
import org.sandbox.service.SearchService;
import org.sandbox.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SearchEngineController.class)
public class SearchEngineControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService;
    @MockBean
    private SearchService searchService;

    @Test
    public void shouldReturnHomePage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index.html"));
    }

    @Test
    public void shouldLoadDocument() throws Exception {
        String content = "Document's content";
        when(storageService.getDocument(1)).thenReturn(content);

        this.mockMvc.perform(get("/load-document?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string(content));
    }

    @Test
    public void shouldUploadDocument() throws Exception {
        String content = "Document's content";
        when(storageService.saveDocument(content)).thenReturn(1l);

        this.mockMvc.perform(post("/upload-document?content=" + content))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void shouldFindDocuments() throws Exception {
        String tokens = "token1, token2";
        List<Long> ids = new ArrayList<>();
        ids.add(1l);
        SearchResult searchResult = new SearchResult(ids, 0l);

        when(searchService.searchFor(tokens)).thenReturn(searchResult);

        this.mockMvc.perform(post("/search?tokens=" + tokens))
                .andExpect(status().isOk())
                .andExpect(content().json("{'result':[1],'failedTasks':0}"));
    }
}
