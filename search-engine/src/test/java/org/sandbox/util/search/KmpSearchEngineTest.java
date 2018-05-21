package org.sandbox.util.search;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class KmpSearchEngineTest {
    private final KmpSearchEngine searchEngine = new KmpSearchEngine();

    @Test
    public void shouldBuildLspTableCase1() throws Exception {
        String testString = "abcabd";
        int[] actual = Whitebox.invokeMethod(searchEngine, "computeLspTable", testString);
        int[] expected = {0, 0, 0, 1, 2, 0};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldBuildLspTableCase2() throws Exception {
        String testString = "abcabdebaabch";
        int[] actual = Whitebox.invokeMethod(searchEngine, "computeLspTable", testString);
        int[] expected = {0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 2, 3, 0};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldBuildLspTableCase3() throws Exception {
        String testString = "abcdef";
        int[] actual = Whitebox.invokeMethod(searchEngine, "computeLspTable", testString);
        int[] expected = {0, 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindToken() {
        String content = "A Method Request is used to pass context information\n" +
                "about a specific method invocation on a Proxy, such\n" +
                "as method parameters and code, from the Proxy to a\n" +
                "Scheduler running in a separate thread. An abstract\n" +
                "Method Request class defines an interface for execut-\n" +
                "ing methods of an Active Object. The interace also\n" +
                "contains guard methods that can be used to determine\n" +
                "when a Method Request’s synchronization constraints\n" +
                "are met. For every Active Object method offered by\n" +
                "the Proxy that requires synchronized access in its Ser-\n" +
                "vant, the abstract Method Request class is subclassed to\n" +
                "create a concrete Method Request class. Instances of\n" +
                "these classes are created by the proxy when its methods\n" +
                "are invoked and contain the specific context informa-\n" +
                "tion necessary to execute these method invocations and\n" +
                "return any results back to clients.\n" +
                "A Scheduler runs in a different thread than its clients,\n" +
                "managing an Activation Queue of Method Requests\n" +
                "that are pending execution. A Scheduler decides which\n" +
                "Method Request to dequeue next and execute on the\n" +
                "Servant that implements this method. This schedul-\n" +
                "ing decision is based on various criteria, such as or-\n" +
                "dering, e.g., the order in which methods are inserted\n" +
                "into the Activation Queue, and synchronization con-\n" +
                "straints, e.g., the fulfillment of certain properties or the\n" +
                "occurrence of specific events, such as space becoming\n" +
                "available for new elements in a bounded data structure.\n" +
                "A Scheduler typically evaluates synchronization con-\n" +
                "The Future construct allows two-way asynchronous invo-\n" +
                "cations that return a value to the client. When a Servant com-\n" +
                "pletes the method execution, it acquires a write lock on the\n" +
                "Future and updates the Future with a result value. Any client\n" +
                "threads that are currently blocked waiting for the result value\n" +
                "are awakened and may access the result value concurrently.\n" +
                "A Future object can be garbage collected after the writer and\n" +
                "all readers no longer reference the Future. In languages like\n" +
                "C++, which do not support garbage collection natively, the\n" +
                "Future objects can be reclaimed when they are no longer in\n" +
                "use via idioms like Counter Pointer [2].\n" +
                "straints by using method request guards.Conductor.Test.";
        String token = "Conductor";
        boolean actual = searchEngine.searchFor(token, content);
        Assert.assertTrue(actual);
    }

    @Test
    public void shouldNotFindAnything() {
        String content = "A Method Request is used to pass context information\n" +
                "about a specific method invocation on a Proxy, such\n" +
                "as method parameters and code, from the Proxy to a\n" +
                "Scheduler running in a separate thread. An abstract\n" +
                "Method Request class defines an interface for execut-\n" +
                "ing methods of an Active Object. The interace also\n" +
                "contains guard methods that can be used to determine\n" +
                "when a Method Request’s synchronization constraints\n" +
                "are met. For every Active Object method offered by\n" +
                "the Proxy that requires synchronized access in its Ser-\n" +
                "vant, the abstract Method Request class is subclassed to\n" +
                "create a concrete Method Request class. Instances of\n" +
                "these classes are created by the proxy when its methods\n" +
                "are invoked and contain the specific context informa-\n" +
                "tion necessary to execute these method invocations and\n" +
                "return any results back to clients.\n" +
                "A Scheduler runs in a different thread than its clients,\n" +
                "managing an Activation Queue of Method Requests\n" +
                "that are pending execution. A Scheduler decides which\n" +
                "Method Request to dequeue next and execute on the\n" +
                "Servant that implements this method. This schedul-\n" +
                "ing decision is based on various criteria, such as or-\n" +
                "dering, e.g., the order in which methods are inserted\n" +
                "into the Activation Queue, and synchronization con-\n" +
                "straints, e.g., the fulfillment of certain properties or the\n" +
                "occurrence of specific events, such as space becoming\n" +
                "available for new elements in a bounded data structure.\n" +
                "A Scheduler typically evaluates synchronization con-\n" +
                "The Future construct allows two-way asynchronous invo-\n" +
                "cations that return a value to the client. When a Servant com-\n" +
                "pletes the method execution, it acquires a write lock on the\n" +
                "Future and updates the Future with a result value. Any client\n" +
                "threads that are currently blocked waiting for the result value\n" +
                "are awakened and may access the result value concurrently.\n" +
                "A Future object can be garbage collected after the writer and\n" +
                "all readers no longer reference the Future. In languages like\n" +
                "C++, which do not support garbage collection natively, the\n" +
                "Future objects can be reclaimed when they are no longer in\n" +
                "use via idioms like Counter Pointer [2].\n" +
                "straints by using method request guards.Conductor.Test.";
        String token = "Bonductor";
        boolean actual = searchEngine.searchFor(token, content);
        Assert.assertFalse(actual);
    }
}
