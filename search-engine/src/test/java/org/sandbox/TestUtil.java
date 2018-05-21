package org.sandbox;

import java.util.List;

public class TestUtil {
    public static String[] toArray(List<String> strings) {
        String[] array = new String[strings.size()];
        return strings.toArray(array);
    }

}
