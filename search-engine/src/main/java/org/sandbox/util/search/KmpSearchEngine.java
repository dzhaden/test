package org.sandbox.util.search;

import org.springframework.stereotype.Component;

@Component
public class KmpSearchEngine implements SearchEngine {
    @Override
    public boolean searchFor(String token, String content) {
        int[] lsp = computeLspTable(token);

        int j = 0;
        for (int i = 0; i < content.length(); i++) {
            while (j > 0 && content.charAt(i) != token.charAt(j)) {
                j = lsp[j - 1];
            }

            if (content.charAt(i) == token.charAt(j)) {
                j++;
                if (j == token.length()) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
     * Takes the prefix which is the first N symbols and tries to find the same sequence
     * in the rest of the token (suffix).
     * E.g. given a token = 'ABCgABCde' - the first 'ABC' is a prefix - the next 'ABC' is a matched suffix,
     * longest-suffix-prefix table looks like: [0, 0, 0, 0, 1, 2, 3, 0, 0] - we should start count from 1
     *
     * or given a token = 'MnABCgrABCr' - here the prefix is 'M' and no matched suffix (no symbol 'M' at all
     * in the rest of the token) lsp table: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,]
     *
     * or given a token = 'abacabab' lsp table for it: [0, 0, 1, 0, 1, 2, 3, 2]
     * */
    private int[] computeLspTable(String token) {
        int[] lsp = new int[token.length()];
        lsp[0] = 0;

        for(int i = 1; i < token.length(); i++) {
            int j = lsp[i - 1];

            while (j > 0 && token.charAt(i) != token.charAt(j)) {
                j = lsp[j - 1];
            }

            if (token.charAt(i) == token.charAt(j)) {
                j++;
            }

            lsp[i] = j;
        }
        return lsp;
    }
}
