package org.sandbox.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenListPreprocessor {

    /*
     * Delete tokens that are subtokens of other tokens - optimization
     * Naive search algorithm is used as we assume that amount of tokens is negligible.
     * */
    public void deleteSubtokens(List<String> tokens) {
        if (tokens.size() < 2) {
            return;
        }
        for (int i = 0; i < tokens.size(); i++) {
            String content = tokens.get(i);
            for (int j = 0; j < tokens.size(); j++) {
                String nextToken = tokens.get(j);
                if (content.length() < nextToken.length() || i == j) {
                    continue;
                }
                if (content.equals(nextToken) || content.indexOf(nextToken) != -1) {
                    tokens.remove(j);
                }
            }
        }
    }
}
