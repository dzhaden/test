package org.sandbox.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServiceController {

    @Value("${search.engine.concurrency.level}")
    private String concurrencyLevel;
    @Value("${search.engine.token.separator}")
    private String tokenSeparator;

    @RequestMapping("/configuration")
    @ResponseBody
    private String configuration() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("search.engine.concurrency.level=").append(concurrencyLevel).append("<br />");
        stringBuilder.append("search.engine.token.separator=").append(tokenSeparator);
        return stringBuilder.toString();
    }
}
