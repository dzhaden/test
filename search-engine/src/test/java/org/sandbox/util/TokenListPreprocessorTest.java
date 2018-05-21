package org.sandbox.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.sandbox.TestUtil.*;

public class TokenListPreprocessorTest {
    private final TokenListPreprocessor preprocessor = new TokenListPreprocessor();

    @Test
    public void shouldDeleteSubtokens() {
        List<String> actual = new ArrayList<>();
        actual.add("abc");
        actual.add("abcd");
        actual.add("dfr");
        actual.add("bc");
        actual.add("abcer");
        actual.add("a");

        List<String> expected = new ArrayList<>();
        expected.add("abcd");
        expected.add("dfr");
        expected.add("abcer");

        preprocessor.deleteSubtokens(actual);

        Assert.assertArrayEquals(toArray(expected), toArray(actual));
    }

    @Test
    public void shouldLeaveAsIs() {
        List<String> actual = new ArrayList<>();
        actual.add("abc");

        List<String> expected = new ArrayList<>();
        expected.add("abc");

        preprocessor.deleteSubtokens(actual);

        Assert.assertArrayEquals(toArray(expected), toArray(actual));
    }
}
