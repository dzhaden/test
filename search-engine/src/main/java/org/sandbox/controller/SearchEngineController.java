package org.sandbox.controller;

import org.sandbox.model.SearchResult;
import org.sandbox.service.SearchService;
import org.sandbox.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class SearchEngineController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StorageService storageService;
    @Autowired
    private SearchService searchService;


    @GetMapping("/")
    private String home() {
        return "index.html";
    }

    @RequestMapping(value = "/load-document", method = RequestMethod.GET)
    private ResponseEntity<String> loadDocument(@RequestParam("id") String id) {
        logger.debug("HTTP request: (urn: {}, id: {})", "/load-document", id);

        long docId;
        try {
            docId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot parse: " + id);
        }

        String content = storageService.getDocument(docId);
        if (content == null) {
            throw new RuntimeException("There is no document with ID: " + docId);
        }

        logger.debug("HTTP response: (urn: {}, content: {})", "/load-document", content);
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @RequestMapping(value = "/upload-document", method = RequestMethod.POST)
    private ResponseEntity<Long> uploadDocument(@RequestParam("content") String content) {
        logger.debug("HTTP request: (urn: {}, content: {})", "/upload-document", content);

        if (content == null || content.isEmpty()) {
            throw new RuntimeException("Empty document");
        }

        long id = storageService.saveDocument(content);

        logger.debug("HTTP response: (urn: {}, id: {})", "/upload-document", id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    private ResponseEntity<SearchResult> search(@RequestParam("tokens") String tokens) {
        logger.debug("HTTP request: (urn: {}, content: {})", "/search", tokens);

        if (tokens == null || tokens.isEmpty()) {
            throw new RuntimeException("Empty tokens");
        }

        SearchResult searchResult = searchService.searchFor(tokens);

        logger.debug("HTTP response: (urn: {}, searchResult: {})", "/search", searchResult);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}